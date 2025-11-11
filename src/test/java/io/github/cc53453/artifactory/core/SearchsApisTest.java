package io.github.cc53453.artifactory.core;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cc53453.Main;
import io.github.cc53453.artifactory.config.MultiArtifactoryConfig;
import io.github.cc53453.artifactory.pojo.ParentManifest;
import io.github.cc53453.artifactory.request.FindParentManifestListsRequest;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = Main.class)
@Slf4j
public class SearchsApisTest {
    private final SearchsApis searchsApis;

    @Autowired
    public SearchsApisTest(MultiArtifactoryConfig configs) {
        this.searchsApis = new SearchsApis(configs.getArtifactorys().get(0));
    }

    /**
     * 确保仓库里至少三个镜像名
     * @throws IOException
     */
//    @Test
    public void testListDockerRepositories() throws IOException {
        List<String> images;
        
        // 从头开始查2个
        images = searchsApis.listDockerRepositories("docker-local", 
                2, "").getRepositories();
        log.warn("images: {}", images);
        Assertions.assertEquals(2, images.size());
        
        // 从中间开始查1个
        images = searchsApis.listDockerRepositories("docker-local", 
                1, images.get(images.size()-1)).getRepositories();
        log.warn("images: {}", images);
        Assertions.assertEquals(1, images.size());
        
        // 从头查全量
        images = searchsApis.listDockerRepositories("docker-local", 
                Integer.MAX_VALUE, "").getRepositories();
        log.warn("images: {}", images);
        Assertions.assertTrue(!images.isEmpty());
        
        // 从末尾开始查1个
        images = searchsApis.listDockerRepositories("docker-local", 
                1, images.get(images.size()-1)).getRepositories();
        log.warn("images: {}", images);
        Assertions.assertTrue(images.isEmpty());
        
        // 仓库不对的情况
        images = searchsApis.listDockerRepositories("docker-local1", 
                Integer.MAX_VALUE, "").getRepositories();
        log.warn("images: {}", images);
        Assertions.assertTrue(images.isEmpty());
    }
    
    @Test
    public void testFindParentManifestLists() throws IOException {
        List<ParentManifest> images;
        FindParentManifestListsRequest request = new FindParentManifestListsRequest();
        
        request.setDockerRepository("jenkins/inbound-agent");
        request.setTag("3341.v0766d82b_dec0-1-jdk21-x86");
        // TODO: artifactory版本不够，测不了
//        images = dockerManager.findParentManifestLists("docker-local", request).getParentManifestLists();
//        log.warn("images: {}", images);
    }
    
    
}
