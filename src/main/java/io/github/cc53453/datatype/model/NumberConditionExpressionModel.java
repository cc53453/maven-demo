package io.github.cc53453.datatype.model;

import io.github.cc53453.datatype.enums.ExpressionType;
import io.github.cc53453.datatype.enums.LogicalOperator;
import lombok.Data;

/**
 * 条件表达式持久化模型
 */
@Data
public class NumberConditionExpressionModel {
	/**
     * 默认的构造函数
     */
    public NumberConditionExpressionModel() {} // NOSONAR
	private Long id;
	private Long parentId;
	private Long treeId;
	private LogicalOperator operator;
	private ExpressionType type;
    private Integer score;
    private Boolean scorable;
    private Integer sortOrder;
}
