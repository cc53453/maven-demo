package io.github.cc53453.file.util;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TxtBatchLineReaderTest {
    @Test
    void test() {
        try(TxtBatchLineReader br = new TxtBatchLineReader("test/test.txt")) {
            List<String> lines;
            // 第一行就超了抛出异常
            try {
                br.next(1);
            }
            catch(IllegalArgumentException e) {
                Assertions.assertTrue(e.getMessage().contains("the first line's length is"));
            }
            
            while((lines = br.next(5000)) != null) {
                lines.forEach(System.out::println);
                Assertions.assertFalse(lines.isEmpty());
            }
        } catch (IOException e) {
            log.error("read txt failed", e);
        }
    }
}
