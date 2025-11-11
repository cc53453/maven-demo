package io.github.cc53453.sm4.config;

import io.github.cc53453.sm4.listener.AutoEncryptLocalFile;
import io.github.cc53453.sm4.annotation.EnableSm4Encrypt;

import java.util.Map;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 获取注解中的filesPath，注入AutoEncryptLocalFile
 */
@Configuration
public class AutoEncryptConfiguration implements ImportAware {
    /**
     * 默认构造函数，由 Spring 自动调用
     */
    public AutoEncryptConfiguration() {}
    
    /**
     * 注解中的filesPath
     */
    private String[] filesPath;
    
    /**
     * 注册AutoEncryptLocalFile的bean
     * @param environment 环境或配置
     * @param sm4Encryptor sm4加密bean
     * @return AutoEncryptLocalFile的bean
     */
    @Bean
    AutoEncryptLocalFile autoEncryptLocalFile(
            Environment environment,
            @Qualifier("jasyptStringEncryptor") StringEncryptor sm4Encryptor) {
        return new AutoEncryptLocalFile(filesPath, environment, sm4Encryptor);
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String, Object> attrs = importMetadata.getAnnotationAttributes(EnableSm4Encrypt.class.getName());
        if (attrs != null) {
            this.filesPath = (String[]) attrs.get("filesPath");
        }
    }
}
