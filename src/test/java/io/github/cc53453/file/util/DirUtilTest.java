package io.github.cc53453.file.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DirUtilTest {
    @Test
    void test() {
        List<File> result = DirUtil.listFilesRecursive(new File("test/").getAbsolutePath());
        result.forEach(f->log.info("{}, {}", f.getParent(), f.getName()));
        assertTrue(!result.isEmpty());
        
        DirUtil.checkDir("test-out/testDir/testDir2");
        assertTrue(FileBaseUtil.exists("test-out/testDir/testDir2"));
        
        DirUtil.deleteRecursive("test-out/testDir");
        assertTrue(!FileBaseUtil.exists("test-out/testDir/"));
    }
}
