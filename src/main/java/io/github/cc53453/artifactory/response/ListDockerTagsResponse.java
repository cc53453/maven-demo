package io.github.cc53453.artifactory.response;

import java.util.List;

import lombok.Data;

/**
 * ListDockerTags api接口的返回
 */
@Data
public class ListDockerTagsResponse {
    /**
     * 镜像名
     */
    private String name;
    /**
     * 镜像tag列表
     */
    private List<String> tags;
}
