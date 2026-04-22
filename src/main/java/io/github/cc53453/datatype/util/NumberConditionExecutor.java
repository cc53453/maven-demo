package io.github.cc53453.datatype.util;

import java.math.BigDecimal;
import java.util.Map;

import io.github.cc53453.datatype.enums.CompareOperator;
import io.github.cc53453.datatype.enums.ExpressionType;
import io.github.cc53453.datatype.enums.LogicalOperator;
import io.github.cc53453.datatype.pojo.NumberConditionEvaluateScoreResult;
import io.github.cc53453.datatype.pojo.NumberConditionExpression;
import io.github.cc53453.datatype.pojo.NumberSingleCondition;

/**
 * 条件表达式执行类 - 支持评分模式
 */
public class NumberConditionExecutor {
    private NumberConditionExecutor() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ========== 原有的布尔评估方法（保持兼容） ==========
    
    /**
     * 评估条件表达式是否满足（布尔模式）
     * @param <T> Number子类
     * @param expression 条件表达式
     * @param data 数据
     * @return data满足expression则返回true
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
            for (NumberConditionExpression<T> child : expression.getChildren()) {
                if (!evaluate(child, data)) {
                    return false;
                }
            }
            return true;
        } else if (operator == LogicalOperator.OR) {
            for (NumberConditionExpression<T> child : expression.getChildren()) {
                if (evaluate(child, data)) {
                    return true;
                }
            }
            return false;
        }
        
        return false;
    }
    
    private static <T extends Number> boolean evaluateCondition(
            NumberSingleCondition<T> condition, 
            Map<String, Object> data) {
        
        Object fieldValue = data.get(condition.getField());
        if (fieldValue == null) {
            return false;
        }
        
        T compareValue = condition.getValue();
        BigDecimal actualValue = toBigDecimal(fieldValue);
        BigDecimal expectedValue = toBigDecimal(compareValue);
        
        if (actualValue == null || expectedValue == null) {
            return false;
        }
        
        int compareResult = actualValue.compareTo(expectedValue);
        CompareOperator operator = condition.getOperator();
        
        switch (operator) {
            case EQ: return compareResult == 0;
            case NE: return compareResult != 0;
            case GT: return compareResult > 0;
            case GE: return compareResult >= 0;
            case LT: return compareResult < 0;
            case LE: return compareResult <= 0;
            default:
                throw new UnsupportedOperationException("Unsupported operator: " + operator);
        }
    }
    
    // ========== 新增的评分模式方法 ==========
    
    /**
     * 评估条件表达式并返回标准化分数（0-1）
     * and分数相加，or分数取最大，not分数取理想最大得分-实际最大得分
     * 
     * @param <T> Number的子类
     * @param expression 条件表达式
     * @param data 输入数据
     * @return 0-1之间的分数
     */
    public static <T extends Number> Double evaluateScore(
            NumberConditionExpression<T> expression,
            Map<String, Object> data) {
        
        NumberConditionEvaluateScoreResult result = evaluateNumberConditionEvaluateScoreResult(expression, data);
        return result.getNormalizedScore();
    }
    
    /**
     * 评估条件表达式并返回详细的评分结果
     * 
     * @param <T> Number的子类
     * @param expression 条件表达式
     * @param data 输入数据
     * @return 包含得分、最高分、标准化分数的结果对象
     */
    public static <T extends Number> NumberConditionEvaluateScoreResult evaluateNumberConditionEvaluateScoreResult(
            NumberConditionExpression<T> expression,
            Map<String, Object> data) {
        
        if (expression == null) {
            return new NumberConditionEvaluateScoreResult(0.0, 0.0);
        }
        
        if(expression.isScorable()) {
        	// 当前节点就是最小的评分单位，下面哪怕还有评分单位，这里截断
        	Integer actualScore = 0;
            if ((expression.getType() == ExpressionType.CONDITION && 
            		evaluateCondition(expression.getCondition(), data)) || 
            		(expression.getType() == ExpressionType.GROUP && 
            		evaluateGroup(expression, data))) {
            	actualScore = expression.getScore();
            }
            return new NumberConditionEvaluateScoreResult(actualScore.doubleValue(), expression.getScore().doubleValue());
        }
        else {
        	// 当前单位不是评分单位
        	if(expression.getType() == ExpressionType.CONDITION) {
        		// 下层是单个表达式，没有分数，只能返回0了
        		return new NumberConditionEvaluateScoreResult(0.0, 0.0);
        	}
    		// 下层还是group，可能在下层有评分单位，本节点只需要and/or/not对分数做变化即可
    		return evaluateGroupScore(expression, data);
        }
    }
    
    /**
     * 评估组合表达式的分数
     * AND: 分数叠加
     * OR: 分数取最大值
     * NOT: 分数取反（1 - 原分数）
     */
    private static <T extends Number> NumberConditionEvaluateScoreResult evaluateGroupScore( // NOSONAR
            NumberConditionExpression<T> expression,
            Map<String, Object> data) {
        if(expression.isScorable()) {
        	// 走到这里不可能是评分单位
        	throw new java.lang.IllegalStateException("invalid expression for evaluateGroupScore");
        }
        
        LogicalOperator operator = expression.getOperator();
        if (operator == LogicalOperator.NOT) {
            // NOT: 对子条件取反
            NumberConditionEvaluateScoreResult childResult = evaluateNumberConditionEvaluateScoreResult(
                expression.getNotExpression(), data);
            
            // NOT的得分 = 满分 - 实际得分
            Double notScore = childResult.getMaxScore() - childResult.getScore();
            
            // NOT的满分不变
            return new NumberConditionEvaluateScoreResult(notScore, childResult.getMaxScore());
        }
        
        // AND/OR 逻辑处理
        if (expression.getChildren() == null || expression.getChildren().isEmpty()) {
            return new NumberConditionEvaluateScoreResult(0.0, 0.0);
        }
        
        if (operator == LogicalOperator.AND) {
            // AND: 分数叠加
            Double totalScore = 0.0;
            Double totalMaxScore = 0.0;
            
            for (NumberConditionExpression<T> child : expression.getChildren()) {
                NumberConditionEvaluateScoreResult childResult = evaluateNumberConditionEvaluateScoreResult(child, data);
                totalScore = totalScore+childResult.getScore();
                totalMaxScore = totalMaxScore+childResult.getMaxScore();
            }
            
            return new NumberConditionEvaluateScoreResult(totalScore, totalMaxScore);
            
        } else if (operator == LogicalOperator.OR) {
            // OR: 取最高分（按标准化分数比较，但返回原始分需要对应）
            NumberConditionEvaluateScoreResult maxResult = null;
            Double bestScore = -1.0;
            
            for (NumberConditionExpression<T> child : expression.getChildren()) {
                NumberConditionEvaluateScoreResult childResult = evaluateNumberConditionEvaluateScoreResult(child, data);
                Double actualScore = childResult.getScore();
                
                if (maxResult == null || 
                        childResult.getMaxScore() >maxResult.getMaxScore()) {
                    maxResult = childResult;
                }
                if(actualScore > bestScore) {
                    bestScore = actualScore;
                }
            }
            
            // OR分支的最高可能得分 = 所有子条件maxScore的最大值
            Double maxMaxScore = maxResult != null ? maxResult.getMaxScore() : 0.0;
            
            // 返回得分和该分支的最高可能得分
            return new NumberConditionEvaluateScoreResult(bestScore, maxMaxScore);
        }
        
        return new NumberConditionEvaluateScoreResult(0.0, 0.0);
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
        } else if (value instanceof Number num) {
            return new BigDecimal(num.toString());
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