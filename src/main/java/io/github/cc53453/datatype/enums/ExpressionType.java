package io.github.cc53453.datatype.enums;

/**
 * 条件表达式的类型，用于构造树形结构
 */
public enum ExpressionType {
	/**
	 * 表示叶子节点，即单个表达式
	 */
    CONDITION,
    /**
     * 表示非叶节点
     */
    GROUP
}
