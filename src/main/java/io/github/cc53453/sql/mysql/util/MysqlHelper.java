package io.github.cc53453.sql.mysql.util;

import com.baomidou.mybatisplus.annotation.TableField;

import io.github.cc53453.datatype.util.ClassReflectHelper;
import io.github.cc53453.datatype.util.DateHelper;

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
     * @param dateFormatStr 时间格式，null时默认 {@link io.github.cc53453.datatype.util.DateHelper#FORMAT_YYYY_MM_DD_HH_MM_SS}
     * @return insert的sql语句
     */
    public static <T> String mybatisEntityToInsertSql(T entity, String tableName, Class<T> entityClass, String dateFormatStr) {
         SimpleDateFormat sdf = dateFormatStr == null
                 ? new SimpleDateFormat(DateHelper.FORMAT_YYYY_MM_DD_HH_MM_SS)
                 : new SimpleDateFormat(dateFormatStr);

         List<String> columns = new ArrayList<>();
         List<String> values = new ArrayList<>();

         for (Field field : entityClass.getDeclaredFields()) {
             TableField tableField = field.getAnnotation(TableField.class);
             if (tableField != null) {
                 Object value = ClassReflectHelper.getFieldValue(entity, field);
                 if (value == null) {
                     continue;
                 }
                 columns.add(tableField.value());
                 values.add(toSqlValue(value, sdf));
             }
         }

         return String.format(
                 "INSERT INTO %s (%s) VALUES (%s);",
                 tableName,
                 String.join(", ", columns),
                 String.join(", ", values)
         );
     }

     /**
      * 把类型判断和 SQL 转义逻辑提取成方法
      * @param value 值, 不可为null
      * @param sdf 时间格式，当value是date时使用 
      * @return sql中的值
      */
     public static String toSqlValue(Object value, SimpleDateFormat sdf) {
         if (value instanceof String || value instanceof Character) {
             return String.format("'%s'", escapeMySqlString(String.valueOf(value)));
         }
         if (value instanceof Boolean boolenValue) {
             return Boolean.TRUE.equals(boolenValue) ? "1" : "0";
         }
         if (value instanceof Date day) {
             return String.format("'%s'", sdf.format(day));
         }
         return value.toString();
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