package io.github.cc53453.artifactory.pojo;

import lombok.Data;

/**
 * 查询的匹配表达式
 */
@Data
public class BuildArtifactsSearchRegexp {
    /**
     * 正则
     */
    private String input;
}
