package io.github.cc53453.desensitize.core;

import lombok.extern.slf4j.Slf4j;

/**
 * 模板类。限定住脱敏的统一入口和大致流程process。
 * @param <T> 配置文件读取到内存里的类
 */
@Slf4j
public abstract class AbstractFileDesensitizeProcessor<T> {
    /**
     * 脱敏策略组合
     */
    protected final DefaultDesensitizeProcessor desensitizeProcessor;

    /**
     * 构造函数
     * @param desensitizeProcessor 由spring自动注入所有实现了DesensitizeStrategy的bean
     */
    protected AbstractFileDesensitizeProcessor(DefaultDesensitizeProcessor desensitizeProcessor) {
        this.desensitizeProcessor = desensitizeProcessor;
    }

    /**
     * 流程固定: 读文件、脱敏、写文件
     * @param path 文件路径
     */
    public final void process(String path) {
        log.info("start for file desensitize: {}", path);
        T data = readFile(path);
        traverseAndDesensitize(data);
        writeFile(data, path);
        log.info("end for file desensitize: {}", path);
    }

    /**
     * 读文件
     * @param path 文件路径
     * @return 文件内容在内存中的对象
     */
    protected abstract T readFile(String path);

    /**
     * 遍历文件内容并脱敏
     * @param data 文件内容
     */
    protected abstract void traverseAndDesensitize(T data);

    /**
     * 写文件
     * @param data 文件内容
     * @param path 文件路径
     */
    protected abstract void writeFile(T data, String path);
}
