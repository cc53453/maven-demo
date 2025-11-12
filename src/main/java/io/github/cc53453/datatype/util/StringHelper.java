package io.github.cc53453.datatype.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 字符串工具
 */
@Slf4j
public class StringHelper {
    /**
     * 工具类，不支持实例化
     */
    private StringHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 处理字符串中的特殊字符，使其可以使用在sql语句中
     * @param value 字符串
     * @return 处理特殊字符后的字符串
     */
    public static String escapeMySqlString(String value) {
        if (value == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : value.toCharArray()) {
            switch (c) {
                case '\0':
                    sb.append("\\0");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\'':
                    sb.append("''"); // MySQL 推荐双单引号
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}
