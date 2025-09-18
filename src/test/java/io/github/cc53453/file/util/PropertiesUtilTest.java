package io.github.cc53453.file.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesUtilTest {
    @Test
    void test() throws IOException {
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
