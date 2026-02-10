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
    private static final Pattern IPV4_PATTERN =
            Pattern.compile("\\b(\\d{1,3}\\.){3}\\d{1,3}\\b");

    /**
     * 是否包含ip
     * @param text 字符串
     * @return 包含ip则返回true
     */
    public static boolean hasIpv4(String text) {
        Matcher matcher = IPV4_PATTERN.matcher(text);
        while (matcher.find()) {
            if (isValidIpv4(matcher.group())) {
                return true;
            }
        }
        return false;
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
            String ip = matcher.group();
            if (isValidIpv4(ip)) {
                matcher.appendReplacement(sb, maskSingleIpv4(ip));
            } else {
                // 非法 IP 原样保留
                matcher.appendReplacement(sb, ip);
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 校验 IPv4 每段是否 0-255
     * @param ip 单个ip
     * @return 是合法ip则返回true
     */
    private static boolean isValidIpv4(String ip) {
        String[] parts = ip.split("\\.");
        for (String part : parts) {
            int n = Integer.parseInt(part);
            if (n < 0 || n > 255) {
                return false;
            }
        }
        return true;
    }

    /**
     * 单个 IPv4 脱敏
     * @param ip 单个ip
     * @return 中间两位数字脱敏成*
     */
    private static String maskSingleIpv4(String ip) {
        String[] parts = ip.split("\\.");
        return parts[0] + ".*.*." + parts[3];
    }
}
