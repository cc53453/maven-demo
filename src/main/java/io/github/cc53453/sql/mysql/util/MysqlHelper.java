package io.github.cc53453.sql.mysql.util;

import com.baomidou.mybatisplus.annotation.TableField;

import io.github.cc53453.datatype.util.ClassReflectHelper;
import io.github.cc53453.date.util.DateUtil;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Mysql工具类
 */
public class MysqlHelper {
    /**
     * 工具类，不支持实例化
     */
    private MysqlHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 把实体类转化为insert语句，只插入非null字段. 要求entity使用mybatis-plus标注数据表列名
     *
     * @param <T>         实体类泛型
     * @param entity      实体类实例
     * @param tableName   表名
     * @param entityClass 实体类Class对象
     * @param dateFormatStr 时间格式，null时默认 {@link io.github.cc53453.date.util.DateUtil#FORMAT_YYYY_MM_DD_HH_MM_SS}
     * @return insert的sql语句
     */
    public static <T> String mybatisEntityToInsertSql(T entity, String tableName, Class<T> entityClass, String dateFormatStr) {
        StringBuilder sb = new StringBuilder();
        StringBuilder valuesSb = new StringBuilder();

        sb.append("INSERT INTO ").append(tableName).append(" (");
        valuesSb.append("VALUES (");
        SimpleDateFormat sdf = dateFormatStr == null ? new SimpleDateFormat(
                DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS) : new SimpleDateFormat(dateFormatStr);
        
        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            TableField tableField = field.getAnnotation(TableField.class);
            if(tableField == null) {
                continue;
            }
            String column = tableField.value();
            Object value = ClassReflectHelper.getFieldValue(entity, field);

            if ( value != null) {
                columns.add(column);
                if (value instanceof String || value instanceof Character) {
                    // 字符串本身如果有单引号，需转义
                    values.add(String.format("'%s'", value.toString().replace("'", "''")));
                } else if (value instanceof Boolean) {
                    // true为1
                    values.add((Boolean) value ? "1" : "0");
                } else if (value instanceof Date) {
                    values.add(String.format("'%s'", sdf.format((Date) value)));
                } else {
                    values.add(value.toString());
                }
            }
        }

        sb.append(String.join(", ", columns)).append(") ");
        valuesSb.append(String.join(", ", values)).append(");");
        sb.append(valuesSb);

        return sb.toString();
    }
    
    

}