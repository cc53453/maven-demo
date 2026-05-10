package io.github.cc53453.datatype.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.cc53453.datatype.enums.CompareOperator;
import io.github.cc53453.datatype.model.NumberConditionExpressionModel;
import io.github.cc53453.datatype.model.NumberSingleConditionModel;
import io.github.cc53453.datatype.pojo.NumberConditionExpression;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class NumberConditionBuilderAndExecutorTest {
	@Test
    void test() {
		// ============ 示例1：简单条件 ============
        // 条件: age > 18
        NumberConditionExpression<Integer> condition1 = NumberConditionBuilder.condition(
            "age", CompareOperator.GT, 18, true, 10
        );
        
        // 测试数据
        Map<String, Object> person1 = new HashMap<>();
        person1.put("age", 25);
        person1.put("name", "张三");
        
        Map<String, Object> person2 = new HashMap<>();
        person2.put("age", 15);
        person2.put("name", "李四");
        
        Assertions.assertTrue(NumberConditionExecutor.evaluate(
        		condition1, person1));
        Assertions.assertFalse(NumberConditionExecutor.evaluate(
        		condition1, person2));

        Assertions.assertEquals(1, NumberConditionExecutor.evaluateScore(
                condition1, person1));
        Assertions.assertEquals(0, NumberConditionExecutor.evaluateScore(
                condition1, person2));
        
        // ============ 示例2：AND组合 ============
        // 条件: age > 18 AND score >= 60
        NumberConditionExpression<Integer> condition2 = NumberConditionBuilder.and(Arrays.asList(
            NumberConditionBuilder.condition("age", CompareOperator.GT, 18, true, 10),
            NumberConditionBuilder.condition("score", CompareOperator.GE, 60, true, 30)
        ), false, null);
        
        Map<String, Object> student1 = new HashMap<>();
        student1.put("age", 20);
        student1.put("score", 85);
        
        Map<String, Object> student2 = new HashMap<>();
        student2.put("age", 20);
        student2.put("score", 55);

        Assertions.assertTrue(NumberConditionExecutor.evaluate(
        		condition2, student1));
        Assertions.assertFalse(NumberConditionExecutor.evaluate(
        		condition2, student2));

        Assertions.assertEquals(1, NumberConditionExecutor.evaluateScore(
                condition2, student1));
        Assertions.assertEquals(0.25, NumberConditionExecutor.evaluateScore(
                condition2, student2));
        
        // ============ 示例3：OR组合 ============
        // 条件: age < 18 OR age > 60
        NumberConditionExpression<Integer> condition3 = NumberConditionBuilder.or(Arrays.asList(
            NumberConditionBuilder.condition("age", CompareOperator.LT, 18, true, 10),
            NumberConditionBuilder.condition("age", CompareOperator.GT, 60, true, 5)
        ), false, null);
        
        Map<String, Object> person3 = new HashMap<>();
        person3.put("age", 70);
        
        Assertions.assertTrue(NumberConditionExecutor.evaluate(
        		condition3, person3));
        
        Assertions.assertEquals(0.5, NumberConditionExecutor.evaluateScore(
                condition3, person3));
        
        // ============ 示例4：NOT组合 ============
        // 条件: NOT (status == "INACTIVE")
        NumberConditionExpression<Integer> condition4 = NumberConditionBuilder.not(
            NumberConditionBuilder.condition("status", CompareOperator.EQ, 0, false, null) // 0表示INACTIVE
        , false, null);
        
        Map<String, Object> activeUser = new HashMap<>();
        activeUser.put("status", 1);

        Assertions.assertTrue(NumberConditionExecutor.evaluate(
        		condition4, activeUser));
        // 测试没有评分系统的情况
        Assertions.assertEquals(0, NumberConditionExecutor.evaluateScore(
                condition4, activeUser));
        
        // ============ 示例5：复杂嵌套条件 ============
        // 条件: (age > 18 AND age < 60) OR (score >= 80 AND level > 3)
        NumberConditionExpression<Integer> condition5 = NumberConditionBuilder.or(Arrays.asList(
            NumberConditionBuilder.and(Arrays.asList(
                NumberConditionBuilder.condition("age", CompareOperator.GT, 18, true, 10),
                NumberConditionBuilder.condition("age", CompareOperator.LT, 60, true, 10)
            ), false, null),
            NumberConditionBuilder.and(Arrays.asList(
                NumberConditionBuilder.condition("score", CompareOperator.GE, 80, true, 10),
                NumberConditionBuilder.condition("level", CompareOperator.GT, 3, true, 10)
            ), false, null)
        ), false, null);
        
        Map<String, Object> candidate1 = new HashMap<>();
        candidate1.put("age", 30);
        candidate1.put("score", 70);
        candidate1.put("level", 2);
        
        Map<String, Object> candidate2 = new HashMap<>();
        candidate2.put("age", 65);
        candidate2.put("score", 90);
        candidate2.put("level", 4);

        Assertions.assertTrue(NumberConditionExecutor.evaluate(
        		condition5, candidate1));
        Assertions.assertTrue(NumberConditionExecutor.evaluate(
        		condition5, candidate2));

        Assertions.assertEquals(1.0, NumberConditionExecutor.evaluateScore(
                condition5, candidate1));
        Assertions.assertEquals(1.0, NumberConditionExecutor.evaluateScore(
                condition5, candidate2));
        
        
        // ============ 示例6：超复杂嵌套条件 ============
        // 条件: 
        // ( (age > 18 AND age < 28, score=10) OR (age > 38 AND age < 48, score=20) ) AND
        // !( (sc>90,score=30 OR sc<10,score=40) AND (level=4,score=50 OR !(level==4),score=60) )
	    // 最高总分：20+（40+60）=120
        NumberConditionExpression<Integer> condition6part1 = NumberConditionBuilder.or(Arrays.asList(
                NumberConditionBuilder.and(Arrays.asList(
                        NumberConditionBuilder.condition("age", CompareOperator.GT, 18, false, null),
                        NumberConditionBuilder.condition("age", CompareOperator.LT, 28, false, null)
                ), true, 10),
                NumberConditionBuilder.and(Arrays.asList(
                    NumberConditionBuilder.condition("age", CompareOperator.GE, 38, false, null),
                    NumberConditionBuilder.condition("age", CompareOperator.LT, 48, false, null)
                ), true, 20)
        ), false, null);
        NumberConditionExpression<Integer> condition6part2 = NumberConditionBuilder.and(Arrays.asList(
				NumberConditionBuilder.or(Arrays.asList(
						NumberConditionBuilder.condition("sc", CompareOperator.GT, 90, true, 30), 
						NumberConditionBuilder.condition("sc", CompareOperator.LT, 10, true, 40)
				), false, null),
        		NumberConditionBuilder.or(Arrays.asList(
        				NumberConditionBuilder.condition("level", CompareOperator.EQ, 4, true, 50),
        				NumberConditionBuilder.not(NumberConditionBuilder.condition("level", CompareOperator.EQ, 4, false, null), true, 60)
				), false, null)
        ), false, null);
        NumberConditionExpression<Integer> condition6 = NumberConditionBuilder.and(Arrays.asList(
        		condition6part1, 
        		NumberConditionBuilder.not(condition6part2, false, null)
        ), false, null);
        
        Map.Entry<List<NumberConditionExpressionModel>, List<NumberSingleConditionModel>> entry = NumberConditionBuilder.flat(condition6);
        Assertions.assertEquals("[NumberConditionExpressionModel(id=1, parentId=null, treeId=1, operator=AND, type=GROUP, score=null, scorable=false, sortOrder=0), NumberConditionExpressionModel(id=2, parentId=1, treeId=1, operator=OR, type=GROUP, score=null, scorable=false, sortOrder=1), NumberConditionExpressionModel(id=3, parentId=2, treeId=1, operator=AND, type=GROUP, score=10, scorable=true, sortOrder=1), NumberConditionExpressionModel(id=4, parentId=3, treeId=1, operator=null, type=CONDITION, score=null, scorable=false, sortOrder=1), NumberConditionExpressionModel(id=5, parentId=3, treeId=1, operator=null, type=CONDITION, score=null, scorable=false, sortOrder=2), NumberConditionExpressionModel(id=6, parentId=2, treeId=1, operator=AND, type=GROUP, score=20, scorable=true, sortOrder=2), NumberConditionExpressionModel(id=7, parentId=6, treeId=1, operator=null, type=CONDITION, score=null, scorable=false, sortOrder=1), NumberConditionExpressionModel(id=8, parentId=6, treeId=1, operator=null, type=CONDITION, score=null, scorable=false, sortOrder=2), NumberConditionExpressionModel(id=9, parentId=1, treeId=1, operator=NOT, type=GROUP, score=null, scorable=false, sortOrder=2), NumberConditionExpressionModel(id=10, parentId=9, treeId=1, operator=AND, type=GROUP, score=null, scorable=false, sortOrder=1), NumberConditionExpressionModel(id=11, parentId=10, treeId=1, operator=OR, type=GROUP, score=null, scorable=false, sortOrder=1), NumberConditionExpressionModel(id=12, parentId=11, treeId=1, operator=null, type=CONDITION, score=30, scorable=true, sortOrder=1), NumberConditionExpressionModel(id=13, parentId=11, treeId=1, operator=null, type=CONDITION, score=40, scorable=true, sortOrder=2), NumberConditionExpressionModel(id=14, parentId=10, treeId=1, operator=OR, type=GROUP, score=null, scorable=false, sortOrder=2), NumberConditionExpressionModel(id=15, parentId=14, treeId=1, operator=null, type=CONDITION, score=50, scorable=true, sortOrder=1), NumberConditionExpressionModel(id=16, parentId=14, treeId=1, operator=NOT, type=GROUP, score=60, scorable=true, sortOrder=2), NumberConditionExpressionModel(id=17, parentId=16, treeId=1, operator=null, type=CONDITION, score=null, scorable=false, sortOrder=1)]=[NumberSingleConditionModel(nodeId=4, treeId=1, field=age, operator=GT, value=18), NumberSingleConditionModel(nodeId=5, treeId=1, field=age, operator=LT, value=28), NumberSingleConditionModel(nodeId=7, treeId=1, field=age, operator=GE, value=38), NumberSingleConditionModel(nodeId=8, treeId=1, field=age, operator=LT, value=48), NumberSingleConditionModel(nodeId=12, treeId=1, field=sc, operator=GT, value=90), NumberSingleConditionModel(nodeId=13, treeId=1, field=sc, operator=LT, value=10), NumberSingleConditionModel(nodeId=15, treeId=1, field=level, operator=EQ, value=4), NumberSingleConditionModel(nodeId=17, treeId=1, field=level, operator=EQ, value=4)]", 
        		entry.toString());
        Assertions.assertEquals(condition6.toString(), 
        		NumberConditionBuilder.toTreeByBigDecimal(entry.getKey(), entry.getValue()).toString());
        
        NumberConditionExpression<BigDecimal> condition7 = NumberConditionBuilder.toTreeByBigDecimal(entry.getKey(), entry.getValue());
        // ( (age > 18 AND age < 28, score=10) OR (age > 38 AND age < 48, score=20) ) AND
        // !( (sc>90,score=30 OR sc<10,score=40) AND (level=4,score=50 OR !(level==4),score=60) )
        // 得分0+（100-（0+50））=50分
        Map<String, Object> candidate3 = new HashMap<>();
        candidate3.put("age", 65);
        candidate3.put("sc", 90);
        candidate3.put("level", 4);
        Assertions.assertFalse(NumberConditionExecutor.evaluate(
        		condition7, candidate3));
        Assertions.assertEquals(0.4166666666666667, NumberConditionExecutor.evaluateScore(
        		condition7, candidate3));
        // 外面再取一个大NOT
        NumberConditionExpression<BigDecimal> condition6not = NumberConditionBuilder.not(condition7, false, null);
        Assertions.assertTrue(NumberConditionExecutor.evaluate(
        		condition6not, candidate3));
        Assertions.assertEquals(0.5833333333333334, NumberConditionExecutor.evaluateScore(
        		condition6not, candidate3));
        
        // ( (age > 18 AND age < 28, score=10) OR (age > 38 AND age < 48, score=20) ) AND
        // !( (sc>90,score=30 OR sc<10,score=40) AND (level=4,score=50 OR !(level==4),score=60) )
        // 得分10+（100-（30+60））=20分
        Map<String, Object> candidate4 = new HashMap<>();
        candidate4.put("age", 20);
        candidate4.put("sc", 91);
        candidate4.put("level", 3);
        Assertions.assertEquals(0.16666666666666666, NumberConditionExecutor.evaluateScore(
        		condition7, candidate4));
        
        // 测试null的情况
        // 得分0+(100-(0+60))=40
        Map<String, Object> candidate5 = new HashMap<>();
        Assertions.assertEquals(0.3333333333333333, NumberConditionExecutor.evaluateScore(
        		condition7, candidate5));
	}
}
