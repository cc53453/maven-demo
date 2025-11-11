package io.github.cc53453.datatype.util;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Get请求方式url中拼接的params构造助手
 */
public class UrlGetParamsBuilder {
    /**
     * 工具类，不支持实例化
     */
    private UrlGetParamsBuilder() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 如果params有值，会拼接成key1=value1&key2=value2&……形式的字符串并以url编码形式返回。
     * @param params key-value对
     * @param charset url编码字符集
     * @return url编码形式的字符串
     */
    public static final String convert(Map<String, String> params, Charset charset) {
        StringBuilder sb = new StringBuilder();
        if(params != null && !params.isEmpty()) {
            params.forEach((k,v)->
                sb.append("&").append(URLEncoder.encode(k, charset))
                    .append("=").append(URLEncoder.encode(v, charset))
            );
            return sb.substring(1);
        }
        return "";
    }
}
