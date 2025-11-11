package io.github.cc53453.artifactory.response;

import java.util.List;

import io.github.cc53453.artifactory.pojo.ParentManifest;
import lombok.Data;

/**
 * FindParentManifestLists api接口的返回
 */
@Data
public class FindParentManifestListsResponse {
    /**
     * 镜像名列表
     */
    private List<ParentManifest> parentManifestLists;
}
