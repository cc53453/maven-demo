package io.github.cc53453.artifactory.response;

import java.util.List;

import lombok.Data;

/**
 * ListDockerRepositories api接口的返回
 */
@Data
public class ListDockerRepositoriesResponse {
    /**
     * 镜像名列表
     */
    private List<String> repositories;
}
