package io.github.cc53453.datatype.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class SortedListByAVLTreeTest {
    @Test
    void test() {
        // 示例1：使用自定义 Model（不实现 Comparable）
        class Person {
            String name;
            int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }

            @Override
            public String toString() {
                return name + "(" + age + ")";
            }
        }

        // 按年龄排序
        SortedListByAVLTree<Person> peopleByAge = new SortedListByAVLTree<>(
                (p1, p2) -> Integer.compare(p1.age, p2.age));

        peopleByAge.add(new Person("Alice", 30));
        peopleByAge.add(new Person("Bob", 25));
        peopleByAge.add(new Person("Charlie", 35));
        peopleByAge.add(new Person("David", 28));

        log.info("按年龄排序: " + peopleByAge.toList());
        log.info("Size: " + peopleByAge.size());
        Assertions.assertEquals("[Bob(25), David(28), Alice(30), Charlie(35)]", 
                peopleByAge.toString());

        // 按姓名排序
        SortedListByAVLTree<Person> peopleByName = new SortedListByAVLTree<>((p1, p2) -> p1.name.compareTo(p2.name));

        peopleByName.add(new Person("Alice", 30));
        peopleByName.add(new Person("Bob", 25));
        peopleByName.add(new Person("Charlie", 35));
        peopleByName.add(new Person("David", 28));

        log.info("按姓名排序: " + peopleByName.toList());
        Assertions.assertEquals("[Alice(30), Bob(25), Charlie(35), David(28)]", 
                peopleByName.toString());

        // 示例2：基本类型（自然排序）
        SortedListByAVLTree<Integer> numbers = new SortedListByAVLTree<>(Integer::compareTo);
        numbers.add(5);
        numbers.add(3);
        numbers.add(7);
        log.info("数字: " + numbers.toList());
        Assertions.assertEquals("[3, 5, 7]", 
                numbers.toString());

        // 示例3：自定义排序（降序）
        SortedListByAVLTree<String> strings = new SortedListByAVLTree<>((a, b) -> b.compareTo(a));
        strings.add("apple");
        strings.add("banana");
        strings.add("cherry");
        log.info("降序字符串: " + strings.toList());
        Assertions.assertEquals("[cherry, banana, apple]", 
                strings.toString());
        
        
        // 测试复杂排序
        SortedListByAVLTree<Integer> numbers2 = new SortedListByAVLTree<>(Integer::compareTo);
        numbers2.add(1);
        numbers2.add(1);
        numbers2.add(2);
        numbers2.add(2);
        numbers2.add(3);
        numbers2.add(3);
        numbers2.add(4);
        numbers2.add(4);
        numbers2.add(5);
        numbers2.add(5);
        numbers2.add(6);
        numbers2.add(6);
        numbers2.add(7);
        numbers2.add(7);
        numbers2.add(8);
        numbers2.add(8);
        numbers2.add(9);
        numbers2.add(9);
        numbers2.add(10);
        numbers2.add(10);
        numbers2.add(11);
        numbers2.add(11);
        log.info("数字: " + numbers2.toList());
        numbers2.printTreeVertical();
        numbers2.remove(4);
        log.info("数字: " + numbers2.toList());
        numbers2.printTreeVertical();
        numbers2.remove(4);
        log.info("数字: " + numbers2.toList());
        numbers2.printTreeVertical();
        numbers2.remove(5);
        log.info("数字: " + numbers2.toList());
        numbers2.printTreeVertical();
        numbers2.remove(5);
        log.info("数字: " + numbers2.toList());
        numbers2.printTreeVertical();
        Assertions.assertEquals("[1, 1, 2, 2, 3, 3, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11]", 
                numbers2.toString());
        Assertions.assertEquals(1, numbers2.getMin());
        Assertions.assertEquals(11, numbers2.getMax());
        
    }
}
