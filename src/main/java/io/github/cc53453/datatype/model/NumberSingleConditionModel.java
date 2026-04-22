package io.github.cc53453.datatype.model;

import io.github.cc53453.datatype.enums.CompareOperator;
import lombok.Data;

/**
 * 单个条件。叶子节点
 */
@Data
public class NumberSingleConditionModel {
	/**
     * 默认的构造函数
     */
    public NumberSingleConditionModel() {} // NOSONAR
	private Long nodeId;
    private String field;
    private CompareOperator operator;
    /**
     * 存数据库时固定存字符串
     */
    private String value;
}
