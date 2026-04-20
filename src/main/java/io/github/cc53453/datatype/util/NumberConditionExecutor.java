package io.github.cc53453.datatype.util;

import java.math.BigDecimal;
import java.util.Map;

import io.github.cc53453.datatype.enums.CompareOperator;
import io.github.cc53453.datatype.enums.ExpressionType;
import io.github.cc53453.datatype.enums.LogicalOperator;
import io.github.cc53453.datatype.pojo.NumberConditionExpression;
import io.github.cc53453.datatype.pojo.NumberSingleCondition;

/**
 * 条件表达式执行类
 */
public class NumberConditionExecutor {
    /**
     * 工具类，不支持实例化
     */
    private NumberConditionExecutor() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
	/**
     * 评估条件表达式是否满足
     * 
     * @param expression 条件表达式
     * @param data 输入数据（通过字段名访问）
     * @return true: 满足条件, false: 不满足条件
     */
    public static <T extends Number> boolean evaluate(
            NumberConditionExpression<T> expression, 
            Map<String, Object> data) {
        
        if (expression == null) {
            return false;
        }
        
        if (expression.getType() == ExpressionType.CONDITION) {
            return evaluateCondition(expression.getCondition(), data);
        } else {
            return evaluateGroup(expression, data);
        }
    }
    
    /**
     * 评估组合表达式
     */
    private static <T extends Number> boolean evaluateGroup(
            NumberConditionExpression<T> expression, 
            Map<String, Object> data) {
        
        LogicalOperator operator = expression.getOperator();
        
        if (operator == LogicalOperator.NOT) {
            return !evaluate(expression.getNotExpression(), data);
        }
        
        if (expression.getChildren() == null || expression.getChildren().isEmpty()) {
            return false;
        }
        
        if (operator == LogicalOperator.AND) {
            // AND: 所有子表达式都必须为true
            for (NumberConditionExpression<T> child : expression.getChildren()) {
                if (!evaluate(child, data)) {
                    return false;
                }
            }
            return true;
        } else if (operator == LogicalOperator.OR) {
            // OR: 至少一个子表达式为true
            for (NumberConditionExpression<T> child : expression.getChildren()) {
                if (evaluate(child, data)) {
                    return true;
                }
            }
            return false;
        }
        
        return false;
    }
    
    /**
     * 评估单个条件
     */
    private static <T extends Number> boolean evaluateCondition(
            NumberSingleCondition<T> condition, 
            Map<String, Object> data) {
        
        // 1. 获取字段值
        Object fieldValue = data.get(condition.getField());
        if (fieldValue == null) {
            return false;
        }
        
        // 2. 获取比较值
        T compareValue = condition.getValue();
        
        // 3. 转换为统一的数值类型进行比较
        BigDecimal actualValue = toBigDecimal(fieldValue);
        BigDecimal expectedValue = toBigDecimal(compareValue);
        
        if (actualValue == null || expectedValue == null) {
            return false;
        }
        
        // 4. 根据操作符进行比较
        CompareOperator operator = condition.getOperator();
        int compareResult = actualValue.compareTo(expectedValue);
        
        switch (operator) {
            case EQ:
                return compareResult == 0;
            case NE:
                return compareResult != 0;
            case GT:
                return compareResult > 0;
            case GE:
                return compareResult >= 0;
            case LT:
                return compareResult < 0;
            case LE:
                return compareResult <= 0;
            default:
                throw new UnsupportedOperationException("Unsupported operator: " + operator);
        }
    }
    
    /**
     * 将对象转换为BigDecimal
     */
    private static BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        
        if (value instanceof BigDecimal bd) {
            return bd;
        } else if (value instanceof Number) {
            return new BigDecimal(value.toString());
        } else if (value instanceof String str) {
            try {
                return new BigDecimal(str);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        
        return null;
    }
}
