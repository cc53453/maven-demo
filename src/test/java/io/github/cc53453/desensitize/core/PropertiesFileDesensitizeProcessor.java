package io.github.cc53453.desensitize.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Component;

import io.github.cc53453.file.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PropertiesFileDesensitizeProcessor extends AbstractFileDesensitizeProcessor<Properties> {

    protected PropertiesFileDesensitizeProcessor(DefaultDesensitizeProcessor desensitizeProcessor) {
        super(desensitizeProcessor);
    }

    @Override
    protected Properties readFile(String path) {
        return PropertiesUtil.loadProperties(path);
    }

    @Override
    protected void traverseAndDesensitize(Properties data) {
        Map<String, String> updates = new HashMap<>();
        data.forEach((key, value)->{
            String newValue = this.desensitizeProcessor.apply(
                    String.valueOf(key), String.valueOf(value));
            updates.put(String.valueOf(key), newValue);
        });
        updates.forEach(data::put);
    }

    @Override
    protected void writeFile(Properties data, String path) {
        PropertiesUtil.writeProperties(path, data, "");
    }

}
