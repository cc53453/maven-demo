package io.github.cc53453.file.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class YamlUtilTest {
    @Test
    void test() {
        Assertions.assertTrue(YamlUtil.isYaml("test/test.yaml"));
        
        JsonNode json = YamlUtil.loadYaml("test/test.yaml");
        Assertions.assertEquals("xxxxx required username=\"\" password=\"pwd\"", 
                json.get("spring").get("kafka").get("properties").get("sasl").get("jaas").asText());
        ObjectNode sasl = (ObjectNode)
                json.get("spring").get("kafka").get("properties").get("sasl");
        sasl.put("jaas", "********");
        
        DirUtil.checkDir("test-out/");
        YamlUtil.writeYaml(json, "test-out/test.yaml");
        json = YamlUtil.loadYaml("test-out/test.yaml");
        Assertions.assertEquals("********", 
                json.get("spring").get("kafka").get("properties").get("sasl").get("jaas").asText());
    }
}
