package io.github.cc53453.desensitize.core;

import org.springframework.stereotype.Component;

import io.github.cc53453.desensitize.pojo.DesensitizeStrategyApplyReturn;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DesensitizeStrategy1 implements DesensitizeStrategy {
    @Override
    public DesensitizeStrategyApplyReturn apply(String key, String value) {
        DesensitizeStrategyApplyReturn result = new DesensitizeStrategyApplyReturn();
        if(key.contains("password")) {
            result.setApplied(true);
            result.setResult("********");
            return result;
        }
        result.setApplied(false);
        result.setResult(value);
        return result;
    }
}
