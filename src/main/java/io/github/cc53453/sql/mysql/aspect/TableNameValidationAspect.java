package io.github.cc53453.sql.mysql.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import io.github.cc53453.sql.mysql.annotation.ValidateTableName;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 表名校验切面
 * 拦截标记了 @ValidateTableName 注解的方法，校验表名参数
 */
@Slf4j
@Aspect
@Component
public class TableNameValidationAspect {
	/**
     * 默认的构造函数
     */
    public TableNameValidationAspect() {} // NOSONAR
    /**
     * 表名正则：字母、数字、下划线，长度 1-64，必须以字母开头
     */
    private static final Pattern TABLE_NAME_PATTERN = Pattern.compile("^[a-zA-Z]\\w{0,63}$");
    
    /**
     * 危险字符/关键字模式（额外的安全检查）
     */
    private static final Pattern DANGEROUS_PATTERN = Pattern.compile(
        "(?i)[;'\"`]|--|/\\*|\\*/|\\b(drop|delete|update|insert|create|alter|truncate|rename|exec|union)\\b"
    );
    
    /**
     * 定义切点：标记了 @ValidateTableName 注解的方法
     */
    @Pointcut("@annotation(io.github.cc53453.sql.mysql.annotation.ValidateTableName)")
    public void validateTableNameMethods() {}
    
    /**
     * 前置通知：方法执行前校验表名
     * @param joinPoint 切点信息
     */
    @Before("validateTableNameMethods()")
    public void validateTableName(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ValidateTableName annotation = method.getAnnotation(ValidateTableName.class);
        
        String methodName = method.getName();
        
        // 确定需要校验的参数索引
        Set<Integer> indicesToValidate = getIndicesToValidate(annotation, args);
        
        for (int i = 0; i < args.length; i++) {
            if (indicesToValidate.contains(i)) {
                Object arg = args[i];
                if (arg instanceof String a) {
                    validateTableName(a, methodName, i);
                }
            }
        }
    }
    
    /**
     * 获取需要校验的参数索引
     */
    private Set<Integer> getIndicesToValidate(ValidateTableName annotation, Object[] args) {
        Set<Integer> indices = new HashSet<>();
        
        int[] specifiedIndices = annotation.indices();
        
        if (specifiedIndices.length == 0) {
            // 未指定索引，校验所有 String 类型的参数
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof String) {
                    indices.add(i);
                }
            }
        } else {
            // 使用指定的索引
            for (int index : specifiedIndices) {
                if (index >= 0 && index < args.length) {
                    indices.add(index);
                } else {
                    log.warn("method: {}, index: {} is out of bound when validateTableName, ignore...", 
                        annotation.getClass().getName(), index);
                }
            }
        }
        
        return indices;
    }
    
    /**
     * 校验单个表名
     */
    private void validateTableName(String tableName, String methodName, int paramIndex) {
        // 1. 空值校验
        if (tableName == null || tableName.trim().isEmpty()) {
            log.error("method: {}, index: {}, table name should not be null", methodName, paramIndex);
            throw new IllegalArgumentException("table name should not be null");
        }
        
        // 去除可能的反引号
        String cleanName = tableName.trim().replace("`", "");
        
        // 2. 格式校验
        if (!TABLE_NAME_PATTERN.matcher(cleanName).matches()) {
            log.error("mothod: {}, index: {}, invalid tablename: {}", methodName, paramIndex, tableName);
            throw new SecurityException(
                String.format("invalid tablename: %s，table should only contains [a-zA-Z0-9_]，and startwith [a-zA-Z], and length < 64", tableName)
            );
        }
        
        // 3. 危险字符校验（双重保险）
        if (DANGEROUS_PATTERN.matcher(cleanName).find()) {
            log.error("mothod: {}, index: {}, invalid tablename: {}, contains danger chars!!", methodName, paramIndex, tableName);
            throw new SecurityException(String.format("invalid tablename: %s, contains danger chars!!", tableName));
        }
        
        log.debug("tablename is safe: {}(param:{}) -> {}", methodName, paramIndex, cleanName);
    }
}