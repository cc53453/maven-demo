package io.github.cc53453.datatype.pojo;

import lombok.Getter;
import lombok.ToString;

/**
 * 通用三元组类
 * @param <K>
 * @param <A>
 * @param <B>
 */
@Getter
@ToString
public class Triple<K, A, B> {
    private final K key;      // 匹配的 date
    private final A object1;
    private final B object2;

    public Triple(K key, A object1, B object2) {
        this.key = key;
        this.object1 = object1;
        this.object2 = object2;
    }
}
