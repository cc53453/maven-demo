package io.github.cc53453.fixedlength.core;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

import io.github.cc53453.datatype.util.ClassReflectHelper;
import io.github.cc53453.fixedlength.annotation.FixedField;
import io.github.cc53453.fixedlength.enums.Align;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据类转定长报文工具
 */
@Slf4j
public class FixedLengthMapper {
    /**
     * 工具类，不支持实例化
     */
    private FixedLengthMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    public static String serialize(Object obj) {
        StringBuilder sb = new StringBuilder();

        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                FixedField ff = field.getAnnotation(FixedField.class);
                if (ff == null) continue;

                Object value = ClassReflectHelper.getFieldValue(obj, field);

                if(value instanceof Number && ff.pad()!='0') {
                    throw new IllegalArgumentException("for number, please use 0 for padding");
                }
                if(value instanceof Number && ff.align()!=Align.LEFT) {
                    throw new IllegalArgumentException("for number, please use left align for padding");
                }
                
                int targetLength = ff.length();
                boolean isMinus = false;
                String text;
                if(value instanceof Number && value != null) {
                    if(((Number)value).doubleValue() < 0) {
                        // 负数，把负号去掉后的字符串补位到指定length-1，然后在最前面补负号
                        
                        text = value.toString().substring(1);
                        isMinus = true;
                        targetLength--;
                    }
                    else {
                        text = value.toString();
                    }
                }
                else {
                    text = value == null ? "" : value.toString();
                }
                
                Charset charset = ff.charset().getCharset();
                byte[] bytes = text.getBytes(charset);

                if (bytes.length > targetLength) {
                    throw new RuntimeException(
                        field.getName().concat(" is too lang, maxLength: ").concat(
                                String.valueOf(ff.length()))
                    );
                }

                String padded;
                int padLength = targetLength - bytes.length;

                if (ff.align() == Align.LEFT) {
                    padded = text + repeat(ff.pad(), padLength);
                } else {
                    padded = repeat(ff.pad(), padLength) + text;
                }

                if(isMinus) {
                    sb.append("-");
                }
                sb.append(padded);
            }
        } catch (Exception e) {
            throw new RuntimeException("定长报文序列化失败", e);
        }

        return sb.toString();
    }
    
    private static String repeat(char c, int n) {
        if (n <= 0) return "";
        return String.valueOf(c).repeat(n);
    }
}
