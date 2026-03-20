package io.github.cc53453.springbatch.common;

import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;

/**
 * 重试策略工厂
 */
public class RetryPolicyFactory {
    /**
     * 默认构造函数，由 Spring 自动调用
     */
    public RetryPolicyFactory() {}  // NOSONAR
    /**
     * 倍数增长等待时间
     * @param initialInterval 初始时间
     * @param multiplier 增长倍数
     * @param maxInterval 最长时间
     * @return 重试策略
     */
    public static BackOffPolicy getExponentialBackOffPolicy(
            Long initialInterval, 
            Double multiplier, 
            Long maxInterval) {
        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(initialInterval);
        policy.setMultiplier(multiplier);
        policy.setMaxInterval(maxInterval);
        return policy;
    }
}
