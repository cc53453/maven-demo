package io.github.cc53453.desensitize.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cc53453.Main;
import io.github.cc53453.file.util.FileBaseUtil;
import io.github.cc53453.file.util.PropertiesUtil;
import io.github.cc53453.file.util.YamlUtil;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = Main.class)
@Slf4j
public class AbstractFileDesensitizeProcessorTest {
    @Autowired
    private PropertiesFileDesensitizeProcessor properties;
    @Autowired
    private YamlFileDesensitizeProcessor yaml;
    
    @Test
    public void test() {
        FileBaseUtil.copyFile("test/test.yaml", "test-out/test.yaml");
        FileBaseUtil.copyFile("test/test.properties", "test-out/test.properties");
        String[] files = {"test-out/test.yaml", "test-out/test.properties"};
        for(String file : files) {
            if(YamlUtil.isYaml(file)) {
                yaml.process(file);
            }
            else if(PropertiesUtil.isProperties(file)) {
                properties.process(file);
            }
        }
    }
}
