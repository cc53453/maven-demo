package io.github.cc53453.artifactory.core;

import java.util.List;

import org.jfrog.artifactory.client.model.File;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cc53453.Main;
import io.github.cc53453.artifactory.config.MultiArtifactoryConfig;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = Main.class)
@Slf4j
public class DeployArtifactApisTest {
    private final DeployArtifactApis deployArtifactApis;

    @Autowired
    public DeployArtifactApisTest(MultiArtifactoryConfig configs) {
        this.deployArtifactApis = new DeployArtifactApis(configs.getArtifactorys().get(0));
    }
    
    @Test
    public void test() {
        List<File> files = deployArtifactApis.uploadFolder("src/main", "java", "generic-local", "unit-test/");
        log.warn("upload: {}", files);
        Assertions.assertTrue(!files.isEmpty());
    }
}
