package io.github.cc53453.artifactory.core;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.client.HttpResponseException;
import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.model.File;

import io.github.cc53453.artifactory.config.MultiArtifactoryConfig.ArtifactoryConfig;
import io.github.cc53453.file.util.DirUtil;
import io.github.cc53453.file.util.FileBaseUtil;
import io.github.cc53453.file.util.FilePathUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于客户端原生的upload单个文件，包装upload目录的功能
 */
@Slf4j
public class DeployArtifactApis {
    /**
     * 制品库客户端
     */
    private Artifactory artifactory;
    
    /**
     * 根据配置的制品库信息获取制品库连接客户端
     * @param config 制品库地址、用户、密码等信息
     */
    public DeployArtifactApis(ArtifactoryConfig config) {
        if(config==null || config.getArtifactoryUrl()==null) {
            throw new IllegalArgumentException("ArtifactoryConfig should at least contain url");
        }
        log.info("init DeployArtifactApis for name: {} by artifactoryUrl: {}, username: {}", 
                config.getName(), config.getArtifactoryUrl(), config.getUsername());
        artifactory = ArtifactoryClientBuilder.create()
                .setUrl(config.getArtifactoryUrl())
                .setUsername(config.getUsername())
                .setPassword(config.getPassword())
                .build();
    }
    
    /**
     * 等价于cd workspace && jfrog rt u folder/ targetPath/ --flat=false
     * @param workspace 工作目录
     * @param folder 要上传的目录
     * @param targetRepo 远程仓库
     * @param targetPath 远程路径
     * @return
     */
    public List<File> uploadFolder(String workspace, String folder, String targetRepo, String targetPath) {
        log.debug("uploadFolder, workspace: {}, folder: {}, targetRepo: {}, targetPath: {}", 
                workspace, folder, targetRepo, targetPath);
        String fullPath = FilePathUtil.concatPath(FilePathUtil.UNIX_SEPARATOR, workspace, folder);
        if(!new java.io.File(fullPath).isDirectory()) {
            return Collections.emptyList();
        }
        
        Path basePath = new java.io.File(workspace).toPath();
        List<File> result = new ArrayList<>();
        for(java.io.File file:DirUtil.listFilesRecursive(fullPath)) {
            String remotePath = FilePathUtil.concatPath(FilePathUtil.UNIX_SEPARATOR, 
                    targetPath, 
                    FilePathUtil.toUnixType(FilePathUtil.relativize(basePath, file.toPath())));
            log.debug("upload {} to {}/{}", file.getAbsolutePath(), targetRepo, remotePath);
            
            try {
                String sha1 = FileBaseUtil.sha1(file);
                File deployed = artifactory.repository("RepoName")
                        .copyBySha1("path/to/newName.txt", sha1)
                        .doUpload();

//                result.add(artifactory
//                        .repository(targetRepo)
//                        .upload(remotePath, file)
//                        .bySha1Checksum()
//                        .doUpload());
            }
            catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }
}
