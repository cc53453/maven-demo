package io.github.cc53453.datatype.pojo;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 遍历JsonNode时记录的信息
 */
@ToString
@Getter
@Setter
public class JacksonJsonWalkContext {
    /**
     * 默认的构造函数
     */
    public JacksonJsonWalkContext() {}
    /**
     * 完整路径，比如app.application[0].name
     */
    private String fullPath;
    /**
     * 完整路径中的最后一个key，比如name。
     * 如果是数组，则返回下标，比如0
     */
    private String currentKey;
    /**
     * 父节点，比如app.application[0]的值
     */
    private JsonNode parent;
    /**
     * 当前节点，比如app.application[0].name的值
     */
    private JsonNode currentValue;
    
    /**
     * 本叶子节点是否为数组的一个元素
     * @return 如果是则返回true
     */
    public boolean isArrayElement() {
        if(parent == null) {
            return false;
        }
        return parent.isArray();
    }
}
