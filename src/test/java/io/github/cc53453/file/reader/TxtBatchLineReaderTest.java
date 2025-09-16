package io.github.cc53453.file.reader;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TxtBatchLineReaderTest {
    @Test
    void test() throws IOException {
        TxtBatchLineReader br = new TxtBatchLineReader("test/test.txt");
        List<String> lines;
        while((lines = br.next(500)) != null) {
            lines.forEach(System.out::println);
            assertTrue(!lines.isEmpty());
        }
        br.close();
    }
}
