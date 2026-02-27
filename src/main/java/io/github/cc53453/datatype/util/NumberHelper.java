package io.github.cc53453.datatype.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * 数字工具类
 */
@Slf4j
public class NumberHelper {
    /**
     * 工具类，不支持实例化
     */
    private NumberHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 负数返回true
     * @param n 数字
     * @return 负数返回true
     */
    public static boolean isNegative(Number n) {
        return n.doubleValue() < 0;
    }
    
    /**
     * 计算绝对值。逻辑和Math.abs保持一致，对于 XXX.MIN_VALUE，返回保持不变
     * @param <T> 只支持BigDecimal，BigInteger，Byte，Double，Float，Integer，Long，Short
     * @param n 数字
     * @return 绝对值
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T abs(T n) {
        if(!isNegative(n)) {
            return n;
        }
        
        if(n instanceof BigDecimal bd) {
            return (T)bd.abs();
        }
        if(n instanceof BigInteger bi) {
            return (T)bi.abs();
        }
        if(n instanceof Byte) {
            // Byte范围是-128~127，计算绝对值有越域风险
            if(n.equals(Byte.MIN_VALUE)) {
                return (T)Byte.valueOf(Byte.MIN_VALUE);
            }
            return (T)Byte.valueOf((byte)Math.abs(n.byteValue()));
        }
        if(n instanceof Double) {
            return (T)Double.valueOf(Math.abs(n.doubleValue()));
        }
        if(n instanceof Float) {
            return (T)Float.valueOf(Math.abs(n.floatValue()));
        }
        if(n instanceof Integer) {
            return (T)Integer.valueOf(Math.abs(n.intValue()));
        }
        if(n instanceof Long) {
            return (T)Long.valueOf(Math.abs(n.longValue()));
        }
        if(n instanceof Short) {
            // Short的范围是-32768~32767，计算绝对值有越域风险
            if(n.equals(Short.MIN_VALUE)) {
                return (T)Short.valueOf(Short.MIN_VALUE);
            }
            return (T)Short.valueOf((short)Math.abs(n.shortValue()));
        }
        
        // 支持基础数据类型，其他诸如AtomicLong等不支持
        throw new IllegalStateException("unexcepted type for number");
    }
}
