package io.github.cc53453.datatype.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

/**
 * list搜索工具，目前支持根据多个索引的等值and搜索.
 * @param <T> Model类型
 * @version 1.0.0
 */
public class ListSearchHelper<T> {
    /** 
     * 原始数据 
     */
    private final List<T> source;

    /**
     * 索引结构：
     * 字段提取函数 -> (字段值 -> 命中对象集合)
     */
    private final Map<String, Map<Object, Set<T>>> indexes;

    /** 
     * 当前查询命中集（null 表示尚未开始过滤） 
     */
    private final Set<T> current;

    /**
     * 不允许直接new本类
     * @param source {@link #source}
     * @param indexes {@link #indexes}
     * @param current {@link #current}
     */
    private ListSearchHelper(List<T> source,
                   Map<String, Map<Object, Set<T>>> indexes,
                   Set<T> current) {
        this.source = source;
        this.indexes = indexes;
        this.current = current;
    }

    /**
     * 构造方法
     * @param <T> 数据类泛型
     * @param list 源数据列表
     * @return ListSearchHelper
     */
    public static <T> ListSearchHelper<T> of(List<T> list) {
        Assert.notNull(list, "source list must not be null");
        return new ListSearchHelper<>(
                List.copyOf(list),
                new HashMap<>(),
                null
        );
    }

    /**
     * 为某个字段建立索引
     * 必须在 get() 之前调用
     * @param <K> keyExtractor方法的返回
     * @param indexName index名称
     * @param keyExtractor 成员变量的get方法
     * @return {@link ListSearchHelper}
     */
    public <K> ListSearchHelper<T> index(String indexName, Function<T, K> keyExtractor) {
        Assert.notNull(keyExtractor, "keyExtractor must not be null");

        Map<Object, Set<T>> map = source.stream()
                .collect(Collectors.groupingBy(
                        keyExtractor,
                        Collectors.toSet()
                ));

        indexes.put(indexName, map);
        return this;
    }

    /**
     * 等值 AND 查询
     * @param <K> 成员变量的类型
     * @param indexName index名称
     * @param value 成员变量的目标值
     * @return {@link ListSearchHelper}
     */
    public <K> ListSearchHelper<T> get(String indexName, K value) {
        Map<Object, Set<T>> index = indexes.get(indexName);
        if (index == null) {
            throw new IllegalStateException(
                    "Index not found ,please call index() first"
            );
        }

        Set<T> hit = index.getOrDefault(value, Set.of());

        Set<T> next;
        if (current == null) {
            next = new HashSet<>(hit);
        } else {
            // 求交集，相当于where …… and ……
            next = current.stream()
                    .filter(hit::contains)
                    .collect(Collectors.toSet());
        }

        return new ListSearchHelper<>(source, indexes, next);
    }

    /**
     * 返回最终结果的List
     * @return 查询结果
     */
    public List<T> toList() {
        if (current == null) {
            return source;
        }
        return List.copyOf(current);
    }

    /**
     * 返回当前查询集合的第一个
     * @return 当前查询集合的第一个
     */
    public Optional<T> first() {
        if (current == null || current.isEmpty()) {
            return Optional.empty();
        }
        return current.stream().findFirst();
    }

    /**
     * 查询集合的大小，如果没查过就是source的大小
     * @return 查询集合大小
     */
    public int size() {
        return current == null ? source.size() : current.size();
    }

    /**
     * 查询集合是否为空
     * @return 空则为true
     */
    public boolean isEmpty() {
        return size() == 0;
    }
}
