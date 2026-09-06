package io.github.cc53453.datatype.pojo;

import lombok.Data;
import java.util.List;

import io.github.cc53453.datatype.enums.ExpressionType;
import io.github.cc53453.datatype.enums.LogicalOperator;

/**
 * 条件表达式 - 支持任意嵌套的逻辑组合
 * @param <T> Number类型的子类，用于指定单个条件中比较的value的类型
 */
@Data
public class NumberConditionExpression<T extends Number> {

	/**
     * 默认的构造函数
     */
    public NumberConditionExpression() {} // NOSONAR
    /**
     * 逻辑操作符：AND, OR, NOT
     * 当type为CONDITION时，此字段无效
     */
    private LogicalOperator operator;
    
    /**
     * 表达式类型：CONDITION（叶子节点）或 GROUP（组合节点）
     */
    private ExpressionType type;
    
    /**
     * 单个条件（当type=CONDITION时使用）
     */
    private NumberSingleCondition<T> condition;
    
    /**
     * 子表达式列表（当type=GROUP时使用）
     * 用于AND/OR组合
     */
    private List<NumberConditionExpression<T>> children;
    
    /**
     * NOT逻辑包装的表达式（当operator=NOT时使用）
     */
    private NumberConditionExpression<T> notExpression;
    
    /**
     * 只有当该节点是评分节点时才有意义
     */
    private Integer score;
    
    /**
     * 是否作为一个评分单元
     */
    private boolean scorable;
}

