package io.github.cc53453.artifactory.request;

import lombok.Data;

/**
 * FindParentManifestLists api接口的请求参数
 */
@Data
public class FindParentManifestListsRequest {
    /**
     * 镜像名
     */
    private String dockerRepository;
    /**
     * 镜像tag
     */
    private String tag;
}