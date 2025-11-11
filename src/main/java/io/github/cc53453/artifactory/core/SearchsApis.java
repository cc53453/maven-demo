package io.github.cc53453.artifactory.core;

import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.ArtifactoryRequest;
import org.jfrog.artifactory.client.ArtifactoryResponse;
import org.jfrog.artifactory.client.impl.ArtifactoryRequestImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import io.github.cc53453.artifactory.config.MultiArtifactoryConfig.ArtifactoryConfig;
import io.github.cc53453.artifactory.request.BuildArtifactsSearchRequest;
import io.github.cc53453.artifactory.request.FindParentManifestListsRequest;
import io.github.cc53453.artifactory.response.ArtifactLatestVersionSearchBasedOnPropertiesResponse;
import io.github.cc53453.artifactory.response.BuildArtifactsSearchResponse;
import io.github.cc53453.artifactory.response.FindParentManifestListsResponse;
import io.github.cc53453.artifactory.response.ListDockerRepositoriesResponse;
import io.github.cc53453.artifactory.response.ListDockerTagsResponse;
import io.github.cc53453.common.enums.ThirdPartySystem;
import io.github.cc53453.common.exception.ThirdPartyApiResponseException;
import io.github.cc53453.datatype.util.UrlGetParamsBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * 实现了https://jfrog.com/help/r/jfrog-rest-apis/searches相关接口
 */
@Slf4j
public class SearchsApis {
    /**
     * 制品库客户端
     */
    private Artifactory artifactory;
    
    /**
     * 根据配置的制品库信息获取制品库连接客户端
     * @param config 制品库地址、用户、密码等信息
     */
    public SearchsApis(ArtifactoryConfig config) {
        if(config==null || config.getArtifactoryUrl()==null) {
            throw new IllegalArgumentException("ArtifactoryConfig should at least contain url");
        }
        log.info("init SearchsApis for name: {} by artifactoryUrl: {}, username: {}", 
                config.getName(), config.getArtifactoryUrl(), config.getUsername());
        artifactory = ArtifactoryClientBuilder.create()
                .setUrl(config.getArtifactoryUrl())
                .setUsername(config.getUsername())
                .setPassword(config.getPassword())
                .build();
    }
    
    /**
     * 获取仓库下的所有镜像名（不包括tag）。Artifactory 5.4.6之后支持
     * @param repo 仓库
     * @param n 返回多少镜像
     * @param last 上一次查询的最后一个镜像名
     * @return 镜像名的List
     * @throws IOException sdk抛错
     */
    public ListDockerRepositoriesResponse listDockerRepositories(String repo, int n, String last) throws IOException {
        log.debug("listDockerRepositories, repo: {}, n: {}, last: {}", repo, n, last);
        ArtifactoryRequest repositoryRequest = new ArtifactoryRequestImpl()
                .apiUrl(String.format("/api/docker/%s/v2/_catalog?n=%s&last=%s", repo, n, last))
                .method(ArtifactoryRequest.Method.GET)
                .responseType(ArtifactoryRequest.ContentType.JSON);
        ArtifactoryResponse response = artifactory.restCall(repositoryRequest);

        // 判断请求是否成功
        if(!response.isSuccessResponse()) {
            log.error("listDockerRepositories failed: {}", response.getRawBody());
            throw new ThirdPartyApiResponseException(response.getRawBody(), 
                    String.valueOf(response.getStatusLine().getStatusCode()), 
                    ThirdPartySystem.Artifactory);
        }

        ListDockerRepositoriesResponse parsedBody = response.parseBody(ListDockerRepositoriesResponse.class);
        return parsedBody;
    }
    
    /**
     * 查引用了本镜像的父混合镜像的tag。Artifactory 7.119.0之后支持
     * @param repo 镜像仓库名
     * @param request 镜像名和tag。详见{@link FindParentManifestListsRequest}
     * @return 父镜像
     * @throws IOException sdk抛错
     */
    public FindParentManifestListsResponse findParentManifestLists(String repo, FindParentManifestListsRequest request) throws IOException {
        log.debug("findParentManifestLists, repo: {}, request: {}", repo, request);
        ArtifactoryRequest repositoryRequest = new ArtifactoryRequestImpl()
                .apiUrl(String.format("/api/v2/%s/parentManifests", repo))
                .method(ArtifactoryRequest.Method.POST)
                .requestBody(request)
                .responseType(ArtifactoryRequest.ContentType.JSON);
        ArtifactoryResponse response = artifactory.restCall(repositoryRequest);

        // 判断请求是否成功
        if(!response.isSuccessResponse()) {
            log.error("findParentManifestLists failed: {}", response.getRawBody());
            throw new ThirdPartyApiResponseException(response.getRawBody(), 
                    String.valueOf(response.getStatusLine().getStatusCode()), 
                    ThirdPartySystem.Artifactory);
        }
        
        FindParentManifestListsResponse parsedBody = response.parseBody(FindParentManifestListsResponse.class);
        return parsedBody;
    }
    
    /**
     * 查镜像tags。Artifactory 5.4.6之后支持
     * @param repo 镜像仓库
     * @param imageName 镜像名
     * @param n 返回多少个tag
     * @param last 上一次查询查到的最后一个tag
     * @return tag的列表
     * @throws IOException sdk抛错
     */
    public ListDockerTagsResponse ListDockerTags(String repo, String imageName, int n, String last) throws IOException {
        log.debug("listDockerTags, repo: {}, imageName: {}, n: {}, last: {}", repo, imageName, n, last);
        ArtifactoryRequest repositoryRequest = new ArtifactoryRequestImpl()
                .apiUrl(String.format("/api/docker/%s/v2/%s/tags/list?n=%s&last=%s", repo, imageName, n, last))
                .method(ArtifactoryRequest.Method.GET)
                .responseType(ArtifactoryRequest.ContentType.JSON);
        ArtifactoryResponse response = artifactory.restCall(repositoryRequest);

        // 判断请求是否成功
        if(!response.isSuccessResponse()) {
            log.error("listDockerTags failed: {}", response.getRawBody());
            throw new ThirdPartyApiResponseException(response.getRawBody(), 
                    String.valueOf(response.getStatusLine().getStatusCode()), 
                    ThirdPartySystem.Artifactory);
        }
        
        ListDockerTagsResponse parsedBody = response.parseBody(ListDockerTagsResponse.class);
        return parsedBody;
    }
    
    /**
     * 以build为维度查制品下载路径。Artifactory 2.6.5之后支持
     * @param request build信息。详见{@link BuildArtifactsSearchRequest}
     * @return 制品下载路径
     * @throws IOException sdk抛错
     */
    public BuildArtifactsSearchResponse buildArtifactsSearch(BuildArtifactsSearchRequest request) throws IOException {
        log.debug("BuildArtifactsSearch, request: {}", request);
        ArtifactoryRequest repositoryRequest = new ArtifactoryRequestImpl()
                .apiUrl("/api/search/buildArtifacts")
                .requestBody(request)
                .method(ArtifactoryRequest.Method.POST)
                .responseType(ArtifactoryRequest.ContentType.JSON);
        ArtifactoryResponse response = artifactory.restCall(repositoryRequest);

        // 判断请求是否成功
        if(!response.isSuccessResponse()) {
            log.error("BuildArtifactsSearch failed: {}", response.getRawBody());
            throw new ThirdPartyApiResponseException(response.getRawBody(), 
                    String.valueOf(response.getStatusLine().getStatusCode()), 
                    ThirdPartySystem.Artifactory);
        }
        
        BuildArtifactsSearchResponse parsedBody = response.parseBody(BuildArtifactsSearchResponse.class);
        return parsedBody;
    }
    
    /**
     * Search for artifacts with the latest value in the version property. 
     * Only artifacts with the version property expressly defined in lowercase will be returned.
     * @param repo 仓库
     * @param path 制品路径
     * @param listFiles 是否把文件列出来
     * @param properties 制品的标签属性
     * @return 最新的版本和制品的清单
     * @throws IOException sdk抛错
     */
    public ArtifactLatestVersionSearchBasedOnPropertiesResponse artifactLatestVersionSearchBasedOnProperties(String repo, String path, boolean listFiles, Map<String, String> properties) throws IOException {
        log.debug("artifactLatestVersionSearchBasedOnProperties, repo: {}, path: {}, listFiles: {}, properties: {}", repo, path, listFiles, properties);
        
        String url = String.format("/api/versions/%s/%s?listFiles=%d", repo, path, listFiles);
        if(properties != null && !properties.isEmpty()) {
            url.concat("&").concat(UrlGetParamsBuilder.convert(properties, StandardCharsets.UTF_8));
        }
        log.debug("artifactLatestVersionSearchBasedOnProperties url: {}", url);
        
        ArtifactoryRequest repositoryRequest = new ArtifactoryRequestImpl()
                .apiUrl(url).method(ArtifactoryRequest.Method.GET)
                .responseType(ArtifactoryRequest.ContentType.JSON);
        ArtifactoryResponse response = artifactory.restCall(repositoryRequest);

        // 判断请求是否成功
        if(!response.isSuccessResponse()) {
            log.error("artifactLatestVersionSearchBasedOnProperties failed: {}", response.getRawBody());
            throw new ThirdPartyApiResponseException(response.getRawBody(), 
                    String.valueOf(response.getStatusLine().getStatusCode()), 
                    ThirdPartySystem.Artifactory);
        }
        
        ArtifactLatestVersionSearchBasedOnPropertiesResponse parsedBody = response.parseBody(ArtifactLatestVersionSearchBasedOnPropertiesResponse.class);
        return parsedBody;
    }
}
