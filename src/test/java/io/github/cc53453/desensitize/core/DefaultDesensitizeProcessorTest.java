package io.github.cc53453.desensitize.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cc53453.Main;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = Main.class)
@Slf4j
class DefaultDesensitizeProcessorTest {
    @Autowired
    private DefaultDesensitizeProcessor defaultDesensitizeProcessor;
    @Test
    void test() {
        Assertions.assertEquals("********", defaultDesensitizeProcessor.apply("password", 
                "value"));
        Assertions.assertEquals("127.*.*.1;127.*.*.2;1222.1111.33.2", defaultDesensitizeProcessor.apply("ip", 
                "127.0.0.1;127.0.0.2;1222.1111.33.2"));
        Assertions.assertEquals("value", defaultDesensitizeProcessor.apply("DesensitizeStrategy3", 
                "value"));
    }
}
