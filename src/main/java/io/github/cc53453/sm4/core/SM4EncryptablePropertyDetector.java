package io.github.cc53453.sm4.core;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;

import lombok.extern.slf4j.Slf4j;

/**
 * 加解密后的字符串的后续处理逻辑
 */
@Slf4j
public class SM4EncryptablePropertyDetector implements EncryptablePropertyDetector {
    /**
     * 加密后的密文会以{SMS4}为前缀
     */
    private final static String ENCRYPT_PREFIX = "{SMS4}";

    /**
     * 默认构造函数，由 Spring 自动调用
     */
    public SM4EncryptablePropertyDetector() {}
    
    /**
     * 加密完成后的密文调用本方法增加前缀标识
     * @param text 密文
     * @return 增加了前缀标识的密文
     */
    public static String addPrefixBeforeEncryptedText(String text) {
        return ENCRYPT_PREFIX.concat(text);
    }
    
    /**
     * 是否加密, 加密或者存在{TODOSMS4}标识前缀时为true，此时会调用解密方法。
     */
    @Override
    public boolean isEncrypted(String property) {
        return (property != null && 
                (property.startsWith(ENCRYPT_PREFIX) || 
                        SM4Encryptor.needEncrypted(property)));
    }

    /**
     * isEncrypted为true时返回的配置值，这里把前缀去掉。
     * 如果是{TODOSMS4}则不去掉前缀，直接在解密方法里再去掉前缀直接返回
     */
    @Override
    public String unwrapEncryptedValue(String property) {
        if(SM4Encryptor.needEncrypted(property)) {
            return property;
        }
        return property.substring(ENCRYPT_PREFIX.length());
    }

}
