package io.github.cc53453.datatype.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import io.github.cc53453.datatype.enums.CompareOperator;
import io.github.cc53453.datatype.enums.ExpressionType;
import io.github.cc53453.datatype.enums.LogicalOperator;
import io.github.cc53453.datatype.model.NumberConditionExpressionModel;
import io.github.cc53453.datatype.model.NumberSingleConditionModel;
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
     * 把拍平的数据结构重构成树形
     * @param expressions 条件表达式
     * @param singles 单组条件
     * @return 树形结构的条件表达式，单组条件中的value转为BigDecimal
     */
    public static NumberConditionExpression<BigDecimal> toTreeByBigDecimal(  // NOSONAR
            List<NumberConditionExpressionModel> expressions,
            List<NumberSingleConditionModel> singles) {

        // 1️. 构建 nodeId -> condition 映射
        Map<Long, NumberSingleConditionModel> conditionMap = new HashMap<>();
        for (NumberSingleConditionModel single : singles) {
            conditionMap.put(single.getNodeId(), single);
        }

        // 2️. 构建 nodeId -> expression 节点
        Map<Long, NumberConditionExpression<BigDecimal>> nodeMap = new HashMap<>();

        for (NumberConditionExpressionModel model : expressions) {
            NumberConditionExpression<BigDecimal> node = new NumberConditionExpression<>();

            node.setType(model.getType());
            node.setOperator(model.getOperator());
            node.setScore(model.getScore());
            node.setScorable(Boolean.TRUE.equals(model.getScorable()));

            // CONDITION 节点：挂 condition
            if (model.getType() == ExpressionType.CONDITION) {
                NumberSingleConditionModel singleModel = conditionMap.get(model.getId());
                if (singleModel != null) {
                    NumberSingleCondition<BigDecimal> condition = new NumberSingleCondition<>();
                    condition.setField(singleModel.getField());
                    condition.setOperator(singleModel.getOperator());

                    // 注意类型转换
                    condition.setValue(new BigDecimal(singleModel.getValue()));

                    node.setCondition(condition);
                }
            }

            nodeMap.put(model.getId(), node);
        }

        // 3️. 构建 parent-child 关系
        NumberConditionExpression<BigDecimal> root = null;

        // 先按 sortOrder 分组（保证顺序）
        Map<Long, List<NumberConditionExpressionModel>> childrenMap = new HashMap<>();

        for (NumberConditionExpressionModel model : expressions) {
            if (model.getParentId() != null) {
                childrenMap
                    .computeIfAbsent(model.getParentId(), k -> new ArrayList<>())
                    .add(model);
            }
        }

        // 排序
        for (List<NumberConditionExpressionModel> list : childrenMap.values()) {
            list.sort(Comparator.comparing(NumberConditionExpressionModel::getSortOrder));
        }

        // 组装
        for (NumberConditionExpressionModel model : expressions) {
            NumberConditionExpression<BigDecimal> node = nodeMap.get(model.getId());

            if (model.getParentId() == null) {
                root = node;
                continue;
            }

            NumberConditionExpression<BigDecimal> parent = nodeMap.get(model.getParentId());

            if (parent.getChildren() == null) {
                parent.setChildren(new ArrayList<>());
            }

            parent.getChildren().add(node);
        }

        // 4️. 处理 NOT（关键点）
        for (NumberConditionExpressionModel model : expressions) {
            if (model.getOperator() == LogicalOperator.NOT) {
                NumberConditionExpression<BigDecimal> node = nodeMap.get(model.getId());

                List<NumberConditionExpression<BigDecimal>> children = node.getChildren();
                if (children != null && children.size() == 1) {
                    node.setNotExpression(children.get(0));
                    node.setChildren(null); // NOT 不用 children
                }
            }
        }

        return root;
    }
    
    /**
     * 把树形结构拍平
     * @param <T> Number的子类
     * @param condition 树形结构的条件表达式
     * @return 拍平后的数据结构，可用于持久化
     */
    public static <T extends Number> Map.Entry<List<NumberConditionExpressionModel>, List<NumberSingleConditionModel>> flat(NumberConditionExpression<T> condition) {
    	List<NumberConditionExpressionModel> expressions = new ArrayList<>();
    	List<NumberSingleConditionModel> singles = new ArrayList<>();
    	
    	// 简单的本地ID生成器（仅用于组装树）
        AtomicLong idGenerator = new AtomicLong(1);
        DSF(condition, null, 1L, expressions, singles, idGenerator);
    	return Map.entry(expressions, singles);
    }
    
    private static <T extends Number> void DSF( // NOSONAR
            NumberConditionExpression<T> node,
            Long parentId,
            Long rootId,
            List<NumberConditionExpressionModel> expressions,
            List<NumberSingleConditionModel> singles,
            AtomicLong idGenerator) {
        if (node == null) return;

        Long currentId = idGenerator.getAndIncrement();

        // 1️. 构建 expression_node
        NumberConditionExpressionModel model = new NumberConditionExpressionModel();
        model.setId(currentId);
        model.setParentId(parentId);
        model.setRootId(rootId);
        model.setOperator(node.getOperator());
        model.setType(node.getType());
        model.setScore(node.getScore());
        model.setScorable(node.isScorable());
        model.setSortOrder(0); // 先给默认，后面children里会覆盖

        expressions.add(model);

        // 2️. 如果是 CONDITION → 写入 single 表
        if (node.getType() == ExpressionType.CONDITION) {
            NumberSingleCondition<T> cond = node.getCondition();
            if (cond != null) {
                NumberSingleConditionModel single = new NumberSingleConditionModel();
                single.setNodeId(currentId);
                single.setField(cond.getField());
                single.setOperator(cond.getOperator());
                single.setValue(cond.getValue() == null ? null : cond.getValue().toString());

                singles.add(single);
            }
            return; // CONDITION 没有 children
        }

        // 3️. GROUP 处理 children（AND / OR）
        if (node.getChildren() != null && !node.getChildren().isEmpty()) {
            int order = 1;
            for (NumberConditionExpression<T> child : node.getChildren()) {

                // 递归前先生成 childId（为了设置 sortOrder）
                Long childId = idGenerator.get();

                DSF(child, currentId, rootId, expressions, singles, idGenerator);

                // 设置 child 的 sortOrder
                // 因为 child 已经被加进 expressions 了，需要回填
                for (NumberConditionExpressionModel e : expressions) {
                    if (e.getId().equals(childId)) {
                        e.setSortOrder(order++);
                        break;
                    }
                }
            }
        }

        // 4️ NOT 处理（只有一个子节点）
        if (node.getOperator() == LogicalOperator.NOT && node.getNotExpression() != null) {

            Long childId = idGenerator.get();

            DSF(node.getNotExpression(), currentId, rootId, expressions, singles, idGenerator);

            // NOT 只有一个 child，sortOrder = 1
            for (NumberConditionExpressionModel e : expressions) {
                if (e.getId().equals(childId)) {
                    e.setSortOrder(1);
                    break;
                }
            }
        }
    }
    
    /**
     * 创建单个条件
     * @param <T> Number子类
     * @param field 要比较的key
     * @param operator 比较操作符
     * @param value 目标value
     * @param scorable 是否是评分最小单位
     * @param score 如果是null，认为不评分
     * @return 类型为CONDITION的条件表达式
     */
    public static <T extends Number> NumberConditionExpression<T> condition(String field, 
                                                  CompareOperator operator, 
                                                  T value, 
                                                  boolean scorable, 
                                                  Integer score) {
        if(score == null) {
        	scorable = false;
        }
    	
    	NumberConditionExpression<T> expr = new NumberConditionExpression<>();
        expr.setType(ExpressionType.CONDITION);
    	expr.setScorable(scorable);
    	expr.setScore(score);
        
        NumberSingleCondition<T> condition = new NumberSingleCondition<>();
        condition.setField(field);
        condition.setOperator(operator);
        condition.setValue(value);
        
        expr.setCondition(condition);
        return expr;
    }
    
    /**
     * AND组合
     * @param <T> Number的子类
     * @param expressions 条件表达式
     * @param scorable 本节点是否可评分
     * @param score 本节点的分数
     * @return 类型为GROUP的NumberConditionExpression
     */
    public static <T extends Number> NumberConditionExpression<T> and(List<NumberConditionExpression<T>> expressions, 
            boolean scorable, 
            Integer score) {
        return combine(LogicalOperator.AND, expressions, scorable, score);
    }
    
    /**
     * OR组合
     * @param <T> Number的子类
     * @param expressions 条件表达式
     * @param scorable 本节点是否可评分
     * @param score 本节点的分数
     * @return 类型为GROUP的NumberConditionExpression
     */
    public static <T extends Number> NumberConditionExpression<T> or(List<NumberConditionExpression<T>> expressions, 
            boolean scorable, 
            Integer score) {
        return combine(LogicalOperator.OR, expressions, scorable, score);
    }
    
    /**
     * NOT组合
     * @param <T> Number的子类
     * @param expression 条件表达式
     * @param scorable 本节点是否可评分
     * @param score 本节点的分数
     * @return 类型为GROUP的NumberConditionExpression
     */
    public static <T extends Number> NumberConditionExpression<T> not(
    		NumberConditionExpression<T> expression, 
            boolean scorable, 
            Integer score) {
        NumberConditionExpression<T> expr = new NumberConditionExpression<>();
        expr.setType(ExpressionType.GROUP);
        expr.setOperator(LogicalOperator.NOT);
        expr.setNotExpression(expression);
        expr.setScorable(scorable);
    	expr.setScore(score);
        return expr;
    }
    
    private static <T extends Number> NumberConditionExpression<T> combine(LogicalOperator operator, 
                                                List<NumberConditionExpression<T>> expressions, 
                                                boolean scorable, 
                                                Integer score) {
        NumberConditionExpression<T> expr = new NumberConditionExpression<>();
        expr.setType(ExpressionType.GROUP);
        expr.setOperator(operator);
        expr.setChildren(expressions);
        expr.setScorable(scorable);
    	expr.setScore(score);
        return expr;
    }
}
