package io.github.cc53453.desensitize.pojo;

import lombok.Data;

/**
 * 脱敏策略apply方法的返回
 */
@Data
public class DesensitizeStrategyApplyReturn {
    /**
     * 是否应用到本脱敏方法，如果应用到则为true，否则为false
     */
    private boolean applied;
    /**
     * 如果应用到本脱敏方法，返回的是脱敏后的字符串，否则返回原来的字符串
     */
    private String result;
    
    /**
     * 默认不应用，result是空字符串
     */
    public DesensitizeStrategyApplyReturn() {
        applied = false;
        result = "";
    }
}
