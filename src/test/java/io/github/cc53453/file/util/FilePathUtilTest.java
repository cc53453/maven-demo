package io.github.cc53453.file.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class FilePathUtilTest {
    @Test
    void test() {
        Assertions.assertTrue(FilePathUtil.isClassPathResource("classpath:application.yml"));
        String path = FilePathUtil.getFullPathByClassPathResource("classpath:application.yml");
        log.info("path: {}", path);
        Assertions.assertTrue(FileBaseUtil.exists(path));
    }
}
