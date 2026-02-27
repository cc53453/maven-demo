package io.github.cc53453.file.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class DirUtilTest {
    @Test
    void test() {
        DirUtil.checkDir("test-out/testDir/testDir2");
        Assertions.assertTrue(FileBaseUtil.exists("test-out/testDir/testDir2"));

        List<File> result = DirUtil.listFilesRecursive(new File("test/").getAbsolutePath());
        result.forEach(f->log.info("{}, {}", f.getParent(), f.getName()));
        Assertions.assertTrue(!result.isEmpty());
        
        result = DirUtil.listDirs("test-out/testDir");
        result.forEach(f->log.info("{}, {}", f.getParent(), f.getName()));
        Assertions.assertTrue(!result.isEmpty());
        
        result = DirUtil.listFiles("test");
        result.forEach(f->log.info("{}, {}", f.getParent(), f.getName()));
        Assertions.assertTrue(!result.isEmpty());
        
        DirUtil.deleteRecursive("test-out/testDir");
        Assertions.assertFalse(FileBaseUtil.exists("test-out/testDir/"));
    }
}
