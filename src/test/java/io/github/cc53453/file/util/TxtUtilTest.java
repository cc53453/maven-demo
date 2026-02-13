package io.github.cc53453.file.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.cc53453.file.dto.TxtDemoDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class TxtUtilTest {
    @Test
    void test() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        List<TxtDemoDTO> list = TxtUtil.readTxt("test/test2.txt", 0, -1, ",", TxtDemoDTO.class);
        list.forEach(l->log.info("{}", l));
        Assertions.assertEquals(1, list.get(0).getId());
        Assertions.assertEquals("李四", list.get(1).getName());

        DirUtil.checkDir("test-out/");
        list.get(2).setId(123);
        TxtUtil.write("test-out/test2.txt", list);
        try(TxtBatchLineReader br = new TxtBatchLineReader("test-out/test2.txt")) {
            List<String> lines = br.next(5000);
            Assertions.assertEquals("123,wangwu", lines.get(2));
        } catch (IOException e) {
            log.error("read txt failed", e);
        }
    }
    

}
