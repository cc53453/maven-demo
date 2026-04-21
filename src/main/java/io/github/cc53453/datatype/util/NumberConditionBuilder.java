package io.github.cc53453.datatype.util;

import java.util.List;

import io.github.cc53453.datatype.enums.CompareOperator;
import io.github.cc53453.datatype.enums.ExpressionType;
import io.github.cc53453.datatype.enums.LogicalOperator;
import io.github.cc53453.datatype.pojo.NumberConditionExpression;
import io.github.cc53453.datatype.pojo.NumberSingleCondition;

/**
 * 条件表达式构建器
 */
public class NumberConditionBuilder {
    /**
     * 工具类，不支持实例化
     */
    private NumberConditionBuilder() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 创建单个条件
     */
    public static <T extends Number> NumberConditionExpression<T> condition(String field, 
                                                  CompareOperator operator, 
                                                  T value, 
                                                  Integer score) {
        NumberConditionExpression<T> expr = new NumberConditionExpression<>();
        expr.setType(ExpressionType.CONDITION);
        
        NumberSingleCondition<T> condition = new NumberSingleCondition<>();
        condition.setField(field);
        condition.setOperator(operator);
        condition.setValue(value);
        condition.setScore(score);
        
        expr.setCondition(condition);
        return expr;
    }
    
    /**
     * AND组合
     */
    public static <T extends Number> NumberConditionExpression<T> and(List<NumberConditionExpression<T>> expressions) {
        return combine(LogicalOperator.AND, expressions);
    }
    
    /**
     * OR组合
     */
    public static <T extends Number> NumberConditionExpression<T> or(List<NumberConditionExpression<T>> expressions) {
        return combine(LogicalOperator.OR, expressions);
    }
    
    /**
     * NOT组合
     */
    public static <T extends Number> NumberConditionExpression<T> not(NumberConditionExpression<T> expression) {
        NumberConditionExpression<T> expr = new NumberConditionExpression<>();
        expr.setType(ExpressionType.GROUP);
        expr.setOperator(LogicalOperator.NOT);
        expr.setNotExpression(expression);
        return expr;
    }
    
    private static <T extends Number> NumberConditionExpression<T> combine(LogicalOperator operator, 
                                                List<NumberConditionExpression<T>> expressions) {
        NumberConditionExpression<T> expr = new NumberConditionExpression<>();
        expr.setType(ExpressionType.GROUP);
        expr.setOperator(operator);
        expr.setChildren(expressions);
        return expr;
    }
}
