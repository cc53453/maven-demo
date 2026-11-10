package io.github.cc53453.datatype.util;

import io.github.cc53453.datatype.pojo.Triple;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

@Slf4j
class ListMatcherHelperTest {
    @Test
    void test(){
        List<Object1> list1 = List.of(
                new Object1(LocalDate.of(2024, 1, 1), "A"),
                new Object1(LocalDate.of(2024, 1, 2), "B")
        );
        List<Object2> list2 = List.of(
                new Object2(LocalDate.of(2024, 1, 1), 100),
                new Object2(LocalDate.of(2024, 1, 2), 200),
                new Object2(LocalDate.of(2024, 1, 2), 201)  // 一对多
        );

        List<Triple<LocalDate, Object1, Object2>> result = ListMatcherHelper.match(
                list1,
                list2,
                Object1::getDate,
                Object2::getDate,
                LocalDate.class,
                Triple::new
        );

        result.forEach(tri->
            Assertions.assertEquals(tri.getObject1().getDate(), tri.getObject2().getDate())
        );
    }

    @AllArgsConstructor
    @Data
    @ToString
    class Object1{
        private LocalDate date;
        private String str;
    }
    @AllArgsConstructor
    @Data
    @ToString
    class Object2{
        private LocalDate date;
        private Integer i;
    }
}
