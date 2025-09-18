package io.github.cc53453.desensitize.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * IP处理工具
 */
@Slf4j
public class IpDesensitizeUtil {
    /**
     * 工具类，不支持实例化
     */
    private IpDesensitizeUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     *  匹配 IPv4
     */
    public static final Pattern IPV4_PATTERN = Pattern.compile(
            "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\." +
                    "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\." +
                    "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\." +
                    "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)"
                );
    
    /**
     * 是否包含ip
     * @param text 字符串
     * @return 包含ip则返回true
     */
    public static boolean hasIpv4(String text) {
        return IPV4_PATTERN.matcher(text).find();
    }
    
    /**
     * ip脱敏
     * @param text 字符串
     * @return ip中间两位会替换为*
     */
    public static String maskIpv4(String text) {
        Matcher matcher = IPV4_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            // 取出四段
            String masked = matcher.group(1) + ".*.*." + matcher.group(4);
            matcher.appendReplacement(sb, masked);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
