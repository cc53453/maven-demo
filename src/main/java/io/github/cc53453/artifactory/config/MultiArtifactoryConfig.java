package io.github.cc53453.artifactory.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import io.micrometer.common.util.StringUtils;
import lombok.Data;

/**
 * 多Artifactory源配置
 */
@Configuration
@ConfigurationProperties(prefix = "jfrog")
@Data
public class MultiArtifactoryConfig {
    /**
     * 默认构造函数，由 Spring 自动调用
     */
    public MultiArtifactoryConfig() {}
    
    /**
     * 可配置多个Artifactory的连接信息。具体配置详见{@link ArtifactoryConfig}
     */
    private List<ArtifactoryConfig> artifactorys;
    
    /**
     * 根据名称获取制品库连接信息，如果没有返回null
     * @param name 名称
     * @return 制品库连接信息
     */
    public ArtifactoryConfig getConfigByName(String name) {
        if(StringUtils.isEmpty(name) || artifactorys == null) {
            return null;
        }
        for(ArtifactoryConfig c:artifactorys) {
            if(name.equals(c.getName())) {
                return c;
            }
        }
        return null;
    }
    
    /**
     * jfrog artifactory相关配置
     */
    @Data
    public static class ArtifactoryConfig {
        /**
         * 默认构造函数，由 Spring 自动调用
         */
        public ArtifactoryConfig() {}

        /**
         * 制品库名称
         */
        private String name;
        /**
         * 制品库地址
         */
        private String artifactoryUrl = "http://localhost:8081/artifactory";
        /**
         * 制品库用户
         */
        private String username;
        /**
         * 制品库密码
         */
        private String password;
    }

}
