package io.github.cc53453.file.util;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.cc53453.file.dto.ShellExecuteResultDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ShellUtilTest {
    @Test
    void test() {
    	ShellExecuteResultDTO result = ShellUtil.execute(List.of("echo", "hello world"));
    	Assertions.assertTrue(result.isSuccess());
    	Assertions.assertEquals("hello world\n", result.getOutput());
    }

}
