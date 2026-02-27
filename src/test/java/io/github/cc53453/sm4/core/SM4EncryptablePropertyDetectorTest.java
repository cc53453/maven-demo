package io.github.cc53453.sm4.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cc53453.Main;

@SpringBootTest(classes = Main.class)
class SM4EncryptablePropertyDetectorTest {
    @Autowired
    private SM4EncryptablePropertyDetector sm4EncryptablePropertyDetector;
    
    @Test
    void test() {
        Assertions.assertTrue(sm4EncryptablePropertyDetector.isEncrypted(
                "{TODOSMS4}xxx"));
        Assertions.assertTrue(sm4EncryptablePropertyDetector.isEncrypted(
                "{SMS4}xxx"));
        Assertions.assertFalse(sm4EncryptablePropertyDetector.isEncrypted(
                "xxx"));
        
        Assertions.assertTrue(SM4EncryptablePropertyDetector
                .addPrefixBeforeEncryptedText("xxx").startsWith("{SMS4}"));
        
        Assertions.assertEquals("xxx", 
                sm4EncryptablePropertyDetector.unwrapEncryptedValue("{SMS4}xxx"));
        Assertions.assertEquals("{TODOSMS4}xxx", 
                sm4EncryptablePropertyDetector.unwrapEncryptedValue("{TODOSMS4}xxx"));
        Assertions.assertEquals("xxx", 
                sm4EncryptablePropertyDetector.unwrapEncryptedValue("123456xxx"));
    }
}
