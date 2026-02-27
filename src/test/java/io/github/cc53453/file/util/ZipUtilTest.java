package io.github.cc53453.file.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ZipUtilTest {
    @Test
    void test() {
        DirUtil.checkDir("test-out/");
        ZipUtil.zip("test/", "test-out/test.zip", "test/");
        assertTrue(ZipUtil.isZip("test-out/test.zip"));
        assertTrue(FileBaseUtil.exists("test-out/test.zip"));
        ZipUtil.unzip("test-out/test.zip", "test-out/");
        assertTrue(FileBaseUtil.exists("test-out/test.yaml"));
        DirUtil.deleteRecursive("test-out/");

        DirUtil.checkDir("test-out/");
        ZipUtil.zip("test/", "test-out/test.zip", "./");
        assertTrue(FileBaseUtil.exists("test-out/test.zip"));
        ZipUtil.unzip("test-out/test.zip", "test-out/");
        assertTrue(FileBaseUtil.exists("test-out/test/test.yaml"));
        FileBaseUtil.deleteFile("test-out/test.zip");
        DirUtil.deleteRecursive("test-out/test/");
    }
}
