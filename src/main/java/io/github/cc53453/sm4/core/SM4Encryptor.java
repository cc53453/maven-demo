package io.github.cc53453.sm4.core;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import io.github.cc53453.sm4.config.SM4Config;
import lombok.extern.slf4j.Slf4j;

/**
 * 加解密工具
 */
@Slf4j
public class SM4Encryptor implements StringEncryptor {
    /**
     * 默认构造函数，由 Spring 自动调用
     */
    public SM4Encryptor() {}
    
    /**
     * sm4配置信息
     */
    @Autowired
    private SM4Config sm4Config;
    
    /**
     * 需要加密的名为需要以{TODOSMS4}为前缀
     */
    private final static String DECRYPT_PREFIX = "{TODOSMS4}";

    /**
     * 判断是否需要加密
     * @param text 字符串
     * @return 如果以{TODOSMS4}打头则返回true
     */
    public static boolean needEncrypted(String text) {
        return (text != null && text.startsWith(DECRYPT_PREFIX));
    }
    
    /**
     * 把需要加密的前缀标识去掉
     * @param text 带前缀标识的字符串
     * @return 去掉标识的字符串
     */
    public static String getPlainTextWithoutPrefix(String text) {
        Assert.notNull(text, "text should not be null");
        return text.substring(DECRYPT_PREFIX.length());
    }
    
    /**
     * 加密
     */
    @Override
    public String encrypt(String message) {
        if(message == null) {
            return null;
        }
        
        byte[] key = sm4Config.getSecretKey().getBytes(StandardCharsets.UTF_8);
        byte[] iv = sm4Config.getIv().getBytes(StandardCharsets.UTF_8);
        byte[] plainTextBytes = message.getBytes(StandardCharsets.UTF_8);
        
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(CBCBlockCipher.newInstance(new SM4Engine()));

        CipherParameters keyParam = new KeyParameter(key);
        ParametersWithIV paramsWithIV = new ParametersWithIV(keyParam, iv);
        cipher.init(true, paramsWithIV); 

        byte[] encryptedBytes = new byte[cipher.getOutputSize(plainTextBytes.length)];
        int len = cipher.processBytes(plainTextBytes, 0, plainTextBytes.length, encryptedBytes, 0);
        try {
            cipher.doFinal(encryptedBytes, len);
            return SM4EncryptablePropertyDetector.addPrefixBeforeEncryptedText(
                    Base64.getEncoder().encodeToString(encryptedBytes));
        } catch (DataLengthException | IllegalStateException | InvalidCipherTextException e) {
            log.error("encrypt failed, {}", e.getMessage());
            throw new IllegalStateException("SM4 加密失败，详见服务端日志");
        }
    }

    /**
     * 解密
     */
    @Override
    public String decrypt(String encryptedMessage) {
        byte[] key = sm4Config.getSecretKey().getBytes(StandardCharsets.UTF_8);
        byte[] iv = sm4Config.getIv().getBytes(StandardCharsets.UTF_8);
        byte[] cipherTextBytes = Base64.getDecoder().decode(
                encryptedMessage.getBytes(StandardCharsets.UTF_8));
        
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(CBCBlockCipher.newInstance(new SM4Engine()));

        CipherParameters keyParam = new KeyParameter(key);
        ParametersWithIV paramsWithIV = new ParametersWithIV(keyParam, iv);
        cipher.init(false, paramsWithIV); 
        
        byte[] decrypted = new byte[cipher.getOutputSize(cipherTextBytes.length)];
        int len = cipher.processBytes(cipherTextBytes, 0, cipherTextBytes.length, decrypted, 0);
        try {
            int finalLen = cipher.doFinal(decrypted, len);
            return new String(decrypted, 0, len + finalLen, StandardCharsets.UTF_8);
        } catch (DataLengthException | IllegalStateException | InvalidCipherTextException e) {
            log.error("decrypt failed, {}", e.getMessage());
            throw new IllegalStateException("SM4 解密失败，详见服务端日志");
        }
    }

}
