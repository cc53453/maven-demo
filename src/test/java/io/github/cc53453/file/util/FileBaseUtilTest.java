package io.github.cc53453.file.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class FileBaseUtilTest {
    @Test
    void test() {
        FileBaseUtil.copyFile("test/test.txt", "test-out/test.txt");
        Assertions.assertTrue(FileBaseUtil.exists("test-out/test.txt"));
        
        Assertions.assertTrue(FileBaseUtil.fileLineCount("test/test.txt") > 0);
        
        FileBaseUtil.deleteFile("test-out/test.txt");
        Assertions.assertFalse(FileBaseUtil.exists("test-out/test.txt"));
    }
}
