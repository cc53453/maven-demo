package io.github.cc53453.datatype.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListMatcherHelper {

    /**
     * 根据两个 List 中元素的匹配规则，返回三元组结果
     *
     * @param list1     第一个列表
     * @param list2     第二个列表
     * @param key1      从 T1 提取用于匹配的 key
     * @param key2      从 T2 提取用于匹配的 key
     * @param keyClass  用于构建 Map 的 key 类型（如 LocalDate.class, String.class）
     * @param tupleCreator 根据 key + t1 + t2 创建结果对象
     * @param <T1>      类型1
     * @param <T2>      类型2
     * @param <K>       匹配 key 类型
     * @param <R>       结果类型
     * @return 匹配结果列表
     */
    public static <T1, T2, K, R> List<R> match(
            List<T1> list1,
            List<T2> list2,
            Function<T1, K> key1,
            Function<T2, K> key2,
            Class<K> keyClass,
            TupleCreator<K, T1, T2, R> tupleCreator) {

        if (list1 == null || list2 == null || list1.isEmpty() || list2.isEmpty()) {
            return Collections.emptyList();
        }

        // 用 Map 分组，兼容一对多的情况（key 相同的多个对象）
        Map<K, List<T2>> map2 = list2.stream()
                .filter(t -> key2.apply(t) != null)
                .collect(Collectors.groupingBy(key2));

        List<R> result = new ArrayList<>();
        for (T1 t1 : list1) {
            K k = key1.apply(t1);
            if (k == null) continue;
            List<T2> matched = map2.get(k);
            if (matched != null) {
                for (T2 t2 : matched) {
                    result.add(tupleCreator.create(k, t1, t2));
                }
            }
        }
        return result;
    }

    @FunctionalInterface
    public interface TupleCreator<K, T1, T2, R> {
        R create(K key, T1 t1, T2 t2);
    }
}
