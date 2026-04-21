package io.github.cc53453.datatype.pojo;

import io.github.cc53453.datatype.enums.CompareOperator;
import lombok.Data;

/**
 * 单个条件
 */
@Data
public class NumberSingleCondition<T extends Number> {
    /**
     * 字段名称
     */
    private String field;
    
    /**
     * 操作符：=, <, <=, >, >=, <>等
     */
    private CompareOperator operator;
    
    /**
     * 比较值（单值）
     */
    private T value;
    
    /**
     * 分数
     */
    private Integer score;
}
