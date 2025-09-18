package io.github.cc53453.desensitize.core;

import org.springframework.stereotype.Component;

import io.github.cc53453.desensitize.pojo.DesensitizeStrategyApplyReturn;
import io.github.cc53453.desensitize.util.IpDesensitizeUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DesensitizeStrategy2 implements DesensitizeStrategy {
    @Override
    public DesensitizeStrategyApplyReturn apply(String key, String value) {
        DesensitizeStrategyApplyReturn result = new DesensitizeStrategyApplyReturn();
        if(IpDesensitizeUtil.hasIpv4(value)) {
            result.setApplied(true);
            result.setResult(IpDesensitizeUtil.maskIpv4(value));
            return result;
        }
        result.setApplied(false);
        result.setResult(value);
        return result;
    }
}
