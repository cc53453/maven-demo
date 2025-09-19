package io.github.cc53453.datatype.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.cc53453.file.util.YamlUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JacksonJsonWalkerTest {
    @Test
    public void test() {
        JsonNode json = YamlUtil.loadYaml("test/test.yaml");
        JacksonJsonWalker.walk(json, context->{
           if(context.getParent() == null) {
               return;
           }
           if(context.isArrayElement()) {
               Assertions.assertEquals(context.getParent().get(Integer.valueOf(context.getCurrentKey())), 
                       context.getCurrentValue());
           }
           else {
               Assertions.assertEquals(context.getParent().get(context.getCurrentKey()), 
                       context.getCurrentValue());
           }
        });
    }
}
