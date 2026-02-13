package io.github.cc53453.sm4.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cc53453.Main;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = Main.class)
@Slf4j
class SM4EncryptorTest {
    @Autowired
    private SM4Encryptor sm4;
    
    @Test
    void test() {
        Assertions.assertEquals("xxx", 
                SM4Encryptor.getPlainTextWithoutPrefix("{TODOSMS4}xxx"));
        Assertions.assertEquals("xxx", 
                SM4Encryptor.getPlainTextWithoutPrefix("123456789Axxx"));
        
        Assertions.assertTrue(SM4Encryptor.needEncrypted("{TODOSMS4}xxx"));
        Assertions.assertFalse(SM4Encryptor.needEncrypted("{SMS4}xxx"));
        Assertions.assertFalse(SM4Encryptor.needEncrypted("xxx"));

        String decode = "test123@";
        String encode = sm4.encrypt(decode);
        log.info("decode: {}, encode: {}", decode, encode);
        Assertions.assertEquals(true, encode.startsWith("{SMS4}"));
        Assertions.assertEquals("test123@", sm4.decrypt(encode.substring(6)));
    }
}
