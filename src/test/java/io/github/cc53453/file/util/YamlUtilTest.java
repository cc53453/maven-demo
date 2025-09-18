package io.github.cc53453.file.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YamlUtilTest {
    @Test
    void test() {
        JsonNode json = YamlUtil.loadYaml("test/test.yaml");
        
        assertEquals("xxxxx required username=\"\" password=\"pwd\"", 
                json.get("spring").get("kafka").get("properties").get("sasl").get("jaas").asText());
        ObjectNode sasl = (ObjectNode)
                json.get("spring").get("kafka").get("properties").get("sasl");
        sasl.put("jaas", "********");
        assertEquals("********", 
                json.get("spring").get("kafka").get("properties").get("sasl").get("jaas").asText());
        
        DirUtil.checkDir("test-out/");
        YamlUtil.writeYaml(json, "test-out/test.yaml");
    }
}
