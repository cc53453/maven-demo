package io.github.cc53453.desensitize.core;

import java.util.List;

import org.springframework.stereotype.Component;

import io.github.cc53453.desensitize.pojo.DesensitizeStrategyApplyReturn;
import lombok.extern.slf4j.Slf4j;

/**
 * 策略组合器。
 */
@Component
@Slf4j
public class DefaultDesensitizeProcessor {
    /**
     * 策略集合
     */
    private final List<DesensitizeStrategy> strategies;

    /**
     * 构造函数
     * @param strategies 所有实现了DesensitizeStrategy的bean都会被spring注入
     */
    public DefaultDesensitizeProcessor(List<DesensitizeStrategy> strategies) {
        this.strategies = strategies;
        log.info("followed strategies will be used: {}", strategies);
    }
    
    /**
     * 按自动注入的strategy依次执行,多个策略命中会多次生效
     * @param key 配置的key
     * @param value 配置的value
     * @return 第一个命中规则的脱敏结果
     */
    public String apply(String key, String value) {
        for (DesensitizeStrategy strategy : strategies) {
            log.debug("start for desensitize, key: {}, value: {}, strategy: {}", 
                    key, value, strategy.getClass());
            DesensitizeStrategyApplyReturn result = strategy.apply(key, value);
            if(result.isApplied()) {
                log.info("desensitizeStrategy applied, key: {}, newValue: {}, strategy: {}", 
                        key, result.getResult(), strategy.getClass());
            }
            value = result.getResult();
        }
        return value;
    }
}
