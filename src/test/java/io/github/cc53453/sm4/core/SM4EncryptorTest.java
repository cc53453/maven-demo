package io.github.cc53453.sm4.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cc53453.Main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jasypt.encryption.StringEncryptor;

@SpringBootTest(classes = Main.class)
public class SM4EncryptorTest {
    @Autowired
    @Qualifier("jasyptStringEncryptor")
    private StringEncryptor sm4;

    @Autowired
    private SM4EncryptablePropertyDetector sm4EncryptablePropertyDetector;
    
    @Test
    public void test() {
        // 假设 UserService 有一个方法可以获取用户的名字
        String decode = "test123@";
        String encode = sm4.encrypt(decode);
        assertEquals(true, sm4EncryptablePropertyDetector.isEncrypted(encode));
        assertEquals("test123@", sm4.decrypt(encode.substring(6)));
    }
}
