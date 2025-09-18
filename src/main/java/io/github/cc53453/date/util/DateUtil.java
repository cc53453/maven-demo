package io.github.cc53453.date.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

/**
 * 时间工具类
 */
@Slf4j
public final class DateUtil {
    /**
     * 年份-月份-日期
     */
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 年份月份日期
     */
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    
    /**
     * 工具类，不支持实例化
     */
    private DateUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 获取当天的时间
     * @param format 时间格式
     * @return 当天时间
     */
    public static String now(String format) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(format));
    }
}
