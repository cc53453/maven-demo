package io.github.cc53453.datatype.pojo;

import lombok.Data;

/**
 * 树的node
 * @param <T> 泛型
 */
@Data
public class TreeNode<T> {
    private T value;
    private TreeNode<T> left;
    private TreeNode<T> right;
    /**
     * 该子树的高度
     */
    private Integer height;
    /**
     * 该value出现了几次
     */
    private Integer count;
    
    public TreeNode(T value) {
        this.value = value;
        this.height = 1;
        this.count = 1;
    }
}
