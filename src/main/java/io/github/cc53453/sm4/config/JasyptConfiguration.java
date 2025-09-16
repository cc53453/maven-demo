package io.github.cc53453.sm4.config;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.ConditionalOnMissingBean;

import io.github.cc53453.sm4.core.SM4EncryptablePropertyDetector;
import io.github.cc53453.sm4.core.SM4Encryptor;

/**
 * 配置类，用于注册jasyptStringEncryptor和encryptablePropertyDetector的bean，才能实现容器启动时自动加解密配置文件
 */
@Configuration
public class JasyptConfiguration {
    /**
     * 默认构造函数，由 Spring 自动调用
     */
    public JasyptConfiguration() {}
    
    //这里的名字必须是jasyptStringEncryptor,不能改动
    @Bean(name = "jasyptStringEncryptor")
    @ConditionalOnMissingBean
    StringEncryptor stringEncryptor(SM4EncryptablePropertyDetector propertyDetector){
        return new SM4Encryptor();
    }

    //这里的名字必须是encryptablePropertyDetector,不能改动
    @Bean(name = "encryptablePropertyDetector")
    @ConditionalOnMissingBean
    SM4EncryptablePropertyDetector encryptablePropertyDetector() {
        return new SM4EncryptablePropertyDetector();
    }
}
