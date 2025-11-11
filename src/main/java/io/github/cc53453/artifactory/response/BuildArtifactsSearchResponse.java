package io.github.cc53453.artifactory.response;

import java.util.List;

import io.github.cc53453.artifactory.pojo.DownloadUri;
import lombok.Data;

/**
 * BuildArtifactsSearch api接口返回
 */
@Data
public class BuildArtifactsSearchResponse {
    private List<DownloadUri> results;
}
