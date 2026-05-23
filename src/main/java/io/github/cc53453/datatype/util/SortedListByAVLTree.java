package io.github.cc53453.datatype.util;

import java.util.*;

import io.github.cc53453.datatype.pojo.TreeNode;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于AVL自平衡二叉搜索树算法的排序列表
 * @param <T> 泛型
 */
@Slf4j
public class SortedListByAVLTree<T> {
    private TreeNode<T> root;
    /**
     * 列表的长度
     */
    private int size;
    private final Comparator<? super T> comparator;

    /**
     * 需传入排序的方法
     * @param comparator 排序方法
     */
    public SortedListByAVLTree(Comparator<? super T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("must privide comparator");
        }
        this.comparator = comparator;
        this.root = null;
        this.size = 0;
    }

    private int compare(T a, T b) {
        return comparator.compare(a, b);
    }

    private int height(TreeNode<T> node) {
        return node == null ? 0 : node.getHeight();
    }

    private void updateHeight(TreeNode<T> node) {
        if (node != null) {
            node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));
        }
    }

    /**
     * 计算平衡因子
     * @param node
     * @return 只有-1~1是平衡的，大于1说明左子树更高，左重，需右旋。
     */
    private int getBalance(TreeNode<T> node) {
        return node == null ? 0 : height(node.getLeft()) - height(node.getRight());
    }

    /**
     * 右旋。演示如下：
     * 
     * 初始：
        y (高度3)
       / \
      x   T3
     / \
    T1  T2
     * 
     * 旋转后：
      x (高度2)
     / \
    T1  y (高度2)
       / \
      T2 T3
     *
     * @param y 待右旋的子树的根节点
     * @return 右旋后的子树的根节点
     */
    private TreeNode<T> rotateRight(TreeNode<T> y) {
        TreeNode<T> x = y.getLeft();
        TreeNode<T> t2 = x.getRight();

        x.setRight(y);
        y.setLeft(t2);

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    /**
     * 左旋。演示如下：
     * 
     * 初始：
        x (高度3)
       / \
      T1  y (高度2)
         / \
        T2 T3
     * 旋转后：
        y (高度2)
       / \
      x   T3
     / \
    T1 T2
     * 
     * @param x 要左旋的子树的根节点
     * @return 左旋后的子树的根节点
     */
    private TreeNode<T> rotateLeft(TreeNode<T> x) {
        TreeNode<T> y = x.getRight();
        TreeNode<T> t2 = y.getLeft();

        y.setLeft(x);
        x.setRight(t2);

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    /**
     * 四种失衡情况：
     * 情况  node的平衡因子   子树的平衡因子 需要做的操作
     * LL  > 1 (左重)    左子树 ≥ 0 只右旋
     * LR  > 1 (左重)    左子树 < 0 先左旋左子树，再右旋
     * RR  < -1 (右重)   右子树 ≤ 0 只左旋
     * RL  < -1 (右重)   右子树 > 0 先右旋右子树，再左旋
     * @param node 要调整的子树的根节点
     * @return 平衡后的子树根节点
     */
    private TreeNode<T> balance(TreeNode<T> node) {
        if (node == null)
            return null;

        updateHeight(node);
        int balance = getBalance(node);

        // Left heavy
        if (balance > 1) {
            if (getBalance(node.getLeft()) < 0) {
                // LR
                node.setLeft(rotateLeft(node.getLeft()));
            }
            return rotateRight(node);
        }

        // Right heavy
        if (balance < -1) {
            if (getBalance(node.getRight()) > 0) {
                // RL
                node.setRight(rotateRight(node.getRight()));
            }
            return rotateLeft(node);
        }

        return node;
    }

    /**
     * 增加元素
     * @param value 元素，不可为null
     */
    public void add(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Null values not allowed");
        }
        root = insert(root, value);
        size++;
    }

    /**
     * 往node位根节点的子树中插入value
     * @param node 子树的根节点
     * @param value 要插入的元素
     * @return 插入并重平衡过的子树的根节点
     */
    private TreeNode<T> insert(TreeNode<T> node, T value) {
        if (node == null) {
            return new TreeNode<>(value);
        }

        int cmp = compare(value, node.getValue());
        if (cmp < 0) {
            node.setLeft(insert(node.getLeft(), value));
        } else if (cmp > 0) {
            node.setRight(insert(node.getRight(), value));
        } else {
            // 相等时，增加计数，不创建新节点
            node.setCount(node.getCount() + 1);
            return node;
        }

        return balance(node);
    }

    /**
     * 移除元素。通过comparator比较为0说明是这个元素。
     * @param value 元素
     */
    public void remove(T value) {
        if (value == null)
            return;

        if (contains(value)) {
            root = remove(root, value);
            size--;
        }
    }

    private TreeNode<T> remove(TreeNode<T> node, T value) {
        if (node == null)
            return null;

        int cmp = compare(value, node.getValue());
        if (cmp < 0) {
            node.setLeft(remove(node.getLeft(), value));
        } else if (cmp > 0) {
            node.setRight(remove(node.getRight(), value));
        } else {
            if (node.getCount() > 1) {
                // 有多个副本，只减少计数
                node.setCount(node.getCount() - 1);
                return node;
            }
            // 只有一个副本，需要删除节点
            if (node.getLeft() == null) {
                return node.getRight();
            } else if (node.getRight() == null) {
                return node.getLeft();
            } else {
                // 找右子树里的最小值替换本节点，因为右子树的最小值  >本节点的所有左子树，并且<本节点的右子树里的其他值。
                TreeNode<T> successor = findMin(node.getRight());
                node.setValue(successor.getValue());
                node.setCount(successor.getCount());
                // 这里要把原来右子树的最小值节点的计数设置成1，这样才能删干净
                successor.setCount(1);
                node.setRight(remove(node.getRight(), successor.getValue()));
            }
        }

        return balance(node);
    }

    /**
     * 获取子树的最小值
     * @param node 子树的根节点
     * @return 子树的最小值
     */
    private TreeNode<T> findMin(TreeNode<T> node) {
        TreeNode<T> current = node;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    /**
     * 检查元素是否存在.comparator比较结果是0说明是该元素
     * @param value 是否存在
     * @return 存在返回true。
     */
    public boolean contains(T value) {
        return findNode(root, value) != null;
    }

    /**
     * 在子树中寻找元素
     * @param node 子树的根节点
     * @param value 元素
     * @return 查找到的该元素的节点
     */
    private TreeNode<T> findNode(TreeNode<T> node, T value) {
        if (node == null)
            return null;

        int cmp = compare(value, node.getValue());
        if (cmp < 0) {
            return findNode(node.getLeft(), value);
        } else if (cmp > 0) {
            return findNode(node.getRight(), value);
        } else {
            return node;
        }
    }

    /**
     * 查找该元素在列表中的下标，从0开始
     * @param value 元素
     * @return 元素所在的下标，如果有重复，返回第一个的下标
     */
    public int getIndex(T value) {
        return getIndex(root, value, 0);
    }

    /**
     * 在子树中查找指定元素
     * @param node 子树的根节点
     * @param value 元素
     * @param offset 目前找到哪里了
     * @return 下标
     */
    private int getIndex(TreeNode<T> node, T value, int offset) {
        if (node == null)
            return -1;

        int leftSize = size(node.getLeft());
        int cmp = compare(value, node.getValue());

        if (cmp < 0) {
            return getIndex(node.getLeft(), value, offset);
        } else if (cmp > 0) {
            return getIndex(node.getRight(), value, offset + leftSize + node.getCount());
        } else {
            // 找到了，返回第一个出现的位置
            return offset + leftSize;
        }
    }

    /**
     * 返回列表的长度
     * @return 列表长度
     */
    public int size() {
        return size;
    }

    /**
     * 返回子树的长度
     * @param node 子树的根节点
     * @return 子树长度
     */
    private int size(TreeNode<T> node) {
        if (node == null)
            return 0;
        return node.getCount() + size(node.getLeft()) + size(node.getRight());
    }

    /**
     * 返回列表。ArrayList
     * @return 列表
     */
    public List<T> toList() {
        List<T> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }

    /**
     * 树转列表
     * @param node 子树的根节点
     * @param result 存储结果
     */
    private void inorderTraversal(TreeNode<T> node, List<T> result) {
        if (node != null) {
            inorderTraversal(node.getLeft(), result);
            // 添加 count 次该值
            for (int i = 0; i < node.getCount(); i++) {
                result.add(node.getValue());
            }
            inorderTraversal(node.getRight(), result);
        }
    }

    /**
     * 获取指定下标的元素
     * @param index 下标
     * @return 元素
     */
    public T get(int index) {
        if (index < 0 || index >= size()) { // 注意用 size() 方法
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
        return getAtIndex(root, index);
    }

    /**
     * 获取子树中指定下标的元素
     * @param node 子树的根节点
     * @param index 下标
     * @return 元素
     */
    private T getAtIndex(TreeNode<T> node, int index) {
        int leftSize = size(node.getLeft()); // 左子树的总元素个数

        if (index < leftSize) {
            return getAtIndex(node.getLeft(), index);
        } else if (index < leftSize + node.getCount()) {
            // 目标在当前节点中（因为有 count 个重复值）
            return node.getValue();
        } else {
            // 在右子树中，需要减去左子树大小和当前节点的所有重复值
            return getAtIndex(node.getRight(), index - leftSize - node.getCount());
        }
    }

    /**
     * 先转为list再toString
     */
    @Override
    public String toString() {
        return toList().toString();
    }

    
    /**
     * 垂直打印树（带连线）
     */
    public void printTreeVertical() {
        printTreeVertical(root, "", true);
    }
    /**
     * 垂直打印子树
     * @param node 子树的根节点
     * @param prefix 前缀
     * @param isTail 是否叶子节点
     */
    private void printTreeVertical(TreeNode<T> node, String prefix, boolean isTail) {
        if (node == null) {
            log.info(prefix + (isTail ? "└── " : "├── ") + "null");
            return;
        }
        
        // 打印当前节点
        log.info(prefix + (isTail ? "└── " : "├── ") 
            + node.getValue() + "(c=" + node.getCount() + ",h=" + node.getHeight() + ")");
        
        // 打印子节点
        if (node.getLeft() != null || node.getRight() != null) {
            String childPrefix = prefix + (isTail ? "    " : "│   ");
            
            // 先打印左子树
            if (node.getLeft() != null) {
                printTreeVertical(node.getLeft(), childPrefix, node.getRight() == null);
            } else if (node.getRight() != null) {
                log.info(childPrefix + "├── null");
            }
            
            // 再打印右子树
            if (node.getRight() != null) {
                printTreeVertical(node.getRight(), childPrefix, true);
            } else if (node.getLeft() != null) {
                log.info(childPrefix + "└── null");
            }
        }
    }
    
    /**
     * 是否为空
     * @return 空为true
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * 获取列表的第一个值，即最小值
     * @return
     */
    public T getMin() {
        return findMin(root).getValue();
    }
    
    /**
     * 获取列表的最后一个值，即最大值
     * @return
     */
    public T getMax() {
        TreeNode<T> current = root;
        while (current.getRight() != null) {
            current = current.getRight();
        }
        return current.getValue();
    }
}
