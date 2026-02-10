package io.github.cc53453.file.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class PropertiesUtilTest {
    @Test
    void test() {
        Properties props = PropertiesUtil.loadProperties("test/test.properties");
        
        System.out.println(props.get("app.users[0].name"));
        assertEquals("xxxxx required username=\"\" password=\"pwd\"", 
                props.get("spring.kafka.properties.sasl.jaas"));
        props.put("spring.kafka.properties.sasl.jaas", "********");
        assertEquals("********", 
                props.get("spring.kafka.properties.sasl.jaas"));

        DirUtil.checkDir("test-out/");
        PropertiesUtil.writeProperties("test-out/test.properties", props, "test");
    }
}
