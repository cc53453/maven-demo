package io.github.cc53453.file.util;

import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class PropertiesUtilTest {
    @Test
    void test() {
        Properties props = PropertiesUtil.loadProperties("test/test.properties");
        log.info("app.users[0].name={}", props.get("app.users[0].name"));
        Assertions.assertEquals("xxxxx required username=\"\" password=\"pwd\"", 
                props.get("spring.kafka.properties.sasl.jaas"));
        
        props.put("spring.kafka.properties.sasl.jaas", "********");
        DirUtil.checkDir("test-out/");
        PropertiesUtil.writeProperties("test-out/test.properties", props, "test");
        props = PropertiesUtil.loadProperties("test-out/test.properties");
        Assertions.assertEquals("********", 
                props.get("spring.kafka.properties.sasl.jaas"));
        
        Assertions.assertTrue(PropertiesUtil.isProperties("test/test.properties"));
    }
}
