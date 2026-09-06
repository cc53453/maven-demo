package io.github.cc53453.sql.mysql.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表名校验注解
 * 标记在需要校验表名参数的方法上，防止 SQL 注入
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateTableName {
    /**
     * 需要校验的参数索引（可选）
     * 如果不指定，默认校验所有 String 类型的参数
     * @return 需要校验的参数索引数组
     */
    int[] indices() default {};
}