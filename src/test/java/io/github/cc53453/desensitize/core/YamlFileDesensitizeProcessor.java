package io.github.cc53453.desensitize.core;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.cc53453.datatype.util.JacksonJsonWalker;
import io.github.cc53453.file.util.YamlUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class YamlFileDesensitizeProcessor extends AbstractFileDesensitizeProcessor<JsonNode> {

    protected YamlFileDesensitizeProcessor(DefaultDesensitizeProcessor desensitizeProcessor, DefaultDesensitizeProcessor defaultDesensitizeProcessor) {
        super(desensitizeProcessor);
    }

    @Override
    protected JsonNode readFile(String path) {
        return YamlUtil.loadYaml(path);
    }

    @Override
    protected void traverseAndDesensitize(JsonNode data) {
        JacksonJsonWalker.walk(data, context->{
            if(!context.getCurrentValue().isTextual()) {
                // 不是字符串不需要脱敏
                return;
            }
            
            String newValue = this.desensitizeProcessor.apply(context.getFullPath(), context.getCurrentValue().asText());
            
            if(context.isArrayElement()) {
                ArrayNode arr = (ArrayNode)context.getParent();
                arr.set(Integer.valueOf(context.getCurrentKey()), newValue);
            }
            else {
                ObjectNode obj = (ObjectNode)context.getParent();
                obj.put(context.getCurrentKey(), newValue);
            }
        });
    }

    @Override
    protected void writeFile(JsonNode data, String path) {
        YamlUtil.writeYaml(data, path);
    }

}
