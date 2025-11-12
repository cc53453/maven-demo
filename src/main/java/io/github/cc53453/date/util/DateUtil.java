package io.github.cc53453.date.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
     * 年份-月份-日期T小时:分钟:秒.毫秒+时区如08:00
     */
    public static final String FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    /**
     * 年份-月份-日期 小时:分钟:秒
     */
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    
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
    
    /**
     * 获取两个日期之间差几天
     * @param start 开始时间，早于end
     * @param end 结束时间
     * @param format 时间格式
     * @return 天数的差值
     */
    public static Long daysBetween(String start, String end, String format) {
        // 解析字符串为 LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date1 = LocalDate.parse(end, formatter);
        LocalDate date2 = LocalDate.parse(start, formatter);
        // 计算相差天数
        return ChronoUnit.DAYS.between(date2, date1); 
    }
    
    /**
     * 字符串转时间
     * @param date 时间字符串
     * @param format 时间格式
     * @return LocalDate类型的时间
     */
    public static LocalDate toLocalDate(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(date, formatter);
    }
}
