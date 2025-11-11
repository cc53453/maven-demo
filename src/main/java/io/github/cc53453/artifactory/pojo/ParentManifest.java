package io.github.cc53453.artifactory.pojo;

import java.util.List;

import lombok.Data;

@Data
public class ParentManifest {
    /**
     * 镜像名
     */
    private String dockerRepository;
    /**
     * 镜像唯一标识digest
     */
    private String digest;
    /**
     * 镜像tag
     */
    private List<String> tags;
}