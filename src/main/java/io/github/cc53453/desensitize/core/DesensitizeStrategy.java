package io.github.cc53453.desensitize.core;

import io.github.cc53453.desensitize.pojo.DesensitizeStrategyApplyReturn;

/**
 * 以key、value为格式的脱敏策略都需要实现本接口
 */
public interface DesensitizeStrategy {
    /**
     * 脱敏入口方法
     * @param key 配置的key
     * @param value 配置的value
     * @return 脱敏后的value，如果不命中策略则返回原来的value即可
     */
    public DesensitizeStrategyApplyReturn apply(String key, String value);
}
