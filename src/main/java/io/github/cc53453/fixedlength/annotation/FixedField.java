package io.github.cc53453.fixedlength.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.cc53453.fixedlength.enums.Align;
import io.github.cc53453.fixedlength.enums.FieldCharset;

/**
 * 定长报文描述
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FixedField {
    /**
     * 需要补充到几位
     * @return 位数
     */
    int length();
    /**
     * 使用什么字符填充补位，默认空格
     * @return 补位用的字符
     */
    char pad() default ' ';
    /**
     * 左补位还是右补位
     * @return 左或右
     */
    Align align() default Align.LEFT;
    /**
     * 什么字符集，比如gbk,utf8
     * @return 字符集
     */
    FieldCharset charset() default FieldCharset.UTF_8;
}
