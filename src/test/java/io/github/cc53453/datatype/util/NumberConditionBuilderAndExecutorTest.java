package io.github.cc53453.datatype.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.cc53453.datatype.enums.CompareOperator;
import io.github.cc53453.datatype.pojo.NumberConditionExpression;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class NumberConditionBuilderAndExecutorTest {
	@Test
    void test() {
		// ============ 示例1：简单条件 ============
        // 条件: age > 18
        NumberConditionExpression<Integer> condition1 = NumberConditionBuilder.condition(
            "age", CompareOperator.GT, 18, 10
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
            NumberConditionBuilder.condition("age", CompareOperator.GT, 18, 10),
            NumberConditionBuilder.condition("score", CompareOperator.GE, 60, 30)
        ));
        
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
            NumberConditionBuilder.condition("age", CompareOperator.LT, 18, 10),
            NumberConditionBuilder.condition("age", CompareOperator.GT, 60, 5)
        ));
        
        Map<String, Object> person3 = new HashMap<>();
        person3.put("age", 70);
        
        Assertions.assertTrue(NumberConditionExecutor.evaluate(
        		condition3, person3));
        
        Assertions.assertEquals(0.5, NumberConditionExecutor.evaluateScore(
                condition3, person3));
        
        // ============ 示例4：NOT组合 ============
        // 条件: NOT (status == "INACTIVE")
        NumberConditionExpression<Integer> condition4 = NumberConditionBuilder.not(
            NumberConditionBuilder.condition("status", CompareOperator.EQ, 0, 10) // 0表示INACTIVE
        );
        
        Map<String, Object> activeUser = new HashMap<>();
        activeUser.put("status", 1);

        Assertions.assertTrue(NumberConditionExecutor.evaluate(
        		condition4, activeUser));

        Assertions.assertEquals(1, NumberConditionExecutor.evaluateScore(
                condition4, activeUser));
        
        // ============ 示例5：复杂嵌套条件 ============
        // 条件: (age > 18 AND age < 60) OR (score >= 80 AND level > 3)
        NumberConditionExpression<Integer> condition5 = NumberConditionBuilder.or(Arrays.asList(
            NumberConditionBuilder.and(Arrays.asList(
                NumberConditionBuilder.condition("age", CompareOperator.GT, 18, 10),
                NumberConditionBuilder.condition("age", CompareOperator.LT, 60, 10)
            )),
            NumberConditionBuilder.and(Arrays.asList(
                NumberConditionBuilder.condition("score", CompareOperator.GE, 80, 10),
                NumberConditionBuilder.condition("level", CompareOperator.GT, 3, 10)
            ))
        ));
        
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
        // !( (score > 90 OR score < 10, score=30) AND (level = 4 OR level = 3, score=40) )
        
        
        // 外面再取一个大NOT
        
	}
}
