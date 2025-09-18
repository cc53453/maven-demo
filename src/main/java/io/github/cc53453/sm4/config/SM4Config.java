package io.github.cc53453.sm4.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * sm4加密配置类，需要定义加解密使用的密钥和初始向量
 */
@Configuration
@ConfigurationProperties(prefix = "sm4")
@Validated
@Data
public class SM4Config {
    /**
     * 默认构造函数，由 Spring 自动调用
     */
    public SM4Config() {}
    
    /**
     * 密钥，必须是 16 字节
     * 例子： "1234567890abcdef"
     */
    @NotBlank(message = "sm4.secret-key 不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{16}$", message = "sm4.secret-key 必须是 16 位字母或数字")
    private String secretKey = "1234567890abcdef";

    /**
     * 初始化向量（IV），必须是 16 字节
     * 例子： "abcdef1234567890"
     */
    @NotBlank(message = "sm4.iv 不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{16}$", message = "sm4.iv 必须是 16 位字母或数字")
    private String iv = "abcdef1234567890";
    
    /**
     * 允许为空，主要是为了方便用户通过-Dsm4.need2encrypt-files-path传参告诉AutoEncryptLocalFile要加密哪些文件。
     * 多个文件用,分割
     */
    private String need2encryptFilesPath;
}
