package io.github.cc53453.artifactory.request;

import java.util.List;

import io.github.cc53453.artifactory.pojo.BuildArtifactsSearchRegexp;
import lombok.Data;

/**
 * BuildArtifactsSearch api接口入参
 */
@Data
public class BuildArtifactsSearchRequest {
    /**
     * 构建名称
     */
    private String buildName;
    /**
     * 构建号
     */
    private Long buildNumber;
    /**
     * 构建状态
     */
    private String buildStatus;
    /**
     * 仓库
     */
    private List<String> repos;
    /**
     * 文件名正则
     */
    private List<BuildArtifactsSearchRegexp> mappings;
}