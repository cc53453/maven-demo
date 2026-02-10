package io.github.cc53453.datatype.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import lombok.extern.slf4j.Slf4j;

/**
 * 类反射工具
 */
@Slf4j
public class ClassReflectHelper {
    /**
     * 工具类，不支持实例化
     */
    private ClassReflectHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 调用getter方法获取类的字段的值
     * @param entity 实体类
     * @param field 字段
     * @return 字段的值，通过getXxx方法获取
     */
    public static Object getFieldValue(Object entity, Field field) {
        try {
            // 获取所有属性描述
            PropertyDescriptor[] props = Introspector.getBeanInfo(entity.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor pd : props) {
                if (pd.getName().equals(field.getName()) && pd.getReadMethod() != null) {
                    return pd.getReadMethod().invoke(entity);
                }
            }
            // 如果找不到 getter，则抛错
            throw new IllegalArgumentException("there's no get method for field " + field.getName());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get value for field " + field.getName(), e);
        }
    }
}
