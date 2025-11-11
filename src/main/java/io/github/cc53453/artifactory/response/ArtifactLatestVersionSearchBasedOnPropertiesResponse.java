package io.github.cc53453.artifactory.response;

import java.util.List;

import io.github.cc53453.artifactory.pojo.Uri;
import lombok.Data;

/**
 * Search for artifacts with the latest value in the version property. 
 * Only artifacts with the version property expressly defined in lowercase will be returned.
 */
@Data
public class ArtifactLatestVersionSearchBasedOnPropertiesResponse {
    /**
     * 版本
     */
    private String version;
    /**
     * 制品路径
     */
    private List<Uri> artifacts;
}
