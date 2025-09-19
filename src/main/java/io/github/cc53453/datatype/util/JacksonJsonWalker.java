package io.github.cc53453.datatype.util;

import java.util.Iterator;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.cc53453.datatype.pojo.JacksonJsonWalkContext;
import lombok.extern.slf4j.Slf4j;

/**
 * jackson的JsonNode遍历工具，遍历的过程中，路径能以 x.x[0].x的方式记录下来。
 */
@Slf4j
public final class JacksonJsonWalker {
    /**
     * json路径以.分割
     */
    private static final String PATH_SPLIT_BY = ".";
    /**
     * 遇到数组，下标记录方式是[i]
     */
    private static final String PATH_ARRAY_START_BY = "[";
    /**
     * 遇到数组，下标记录方式是[i]
     */
    private static final String PATH_ARRAY_END_BY = "]";
    
    /**
     * 工具类，不支持实例化
     */
    private JacksonJsonWalker() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 遍历JsonNode. 注意本方法只能支持遍历时对叶子节点做value值的修改或查询。
     * 任何新增、删除节点，改变了树的结构会导致遍历出现问题
     * @param node 需要遍历的JsonNode对象
     * @param consumer 遍历到叶子节点时调用的方法。传参为(path,node)，path是叶子节点的完整路径，node是该叶子节点的JsonNode对象。
     */
    public static void walk(JsonNode node, Consumer<JacksonJsonWalkContext> consumer) {
        walk(node, "", null, null, consumer);
    }

    private static void walk(JsonNode node, String path, JsonNode parent, String currentKey, Consumer<JacksonJsonWalkContext> consumer) {
        if (node.isObject()) {
            Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                JsonNode value = node.get(key);
                String currentPath = path.isEmpty() ? key : 
                    path.concat(PATH_SPLIT_BY).concat(key);
                walk(value, currentPath, node, key, consumer);
            }
        }
        else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                String sub = PATH_ARRAY_START_BY.concat(String.valueOf(i)).concat(PATH_ARRAY_END_BY);
                String currentPath = path.concat(sub);
                walk(node.get(i), currentPath, node, String.valueOf(i), consumer);
            }
        }
        else {
            JacksonJsonWalkContext context = new JacksonJsonWalkContext();
            context.setFullPath(path);
            context.setParent(parent);
            context.setCurrentValue(node);
            context.setCurrentKey(currentKey);
            consumer.accept(context);
        }
    }
}
