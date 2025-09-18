package io.github.cc53453.file.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZipUtilTest {
    @Test
    void test() {
        ZipUtil.zip("test/", "test.zip");
        assertTrue(FileBaseUtil.exists("test.zip"));
        ZipUtil.unzip("test.zip", "test-out/");
        assertTrue(FileBaseUtil.exists("test-out/"));
        FileBaseUtil.deleteFile("test.zip");
    }
}
