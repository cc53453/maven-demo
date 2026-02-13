package io.github.cc53453.datatype.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ByteArrayReaderTest {
    @Test
    void test() {
        byte[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteArrayReader bar = new ByteArrayReader(arr);
        Assertions.assertEquals(1, bar.next(2)[1]);
        Assertions.assertEquals(4, bar.next(3)[2]);
        Assertions.assertEquals(5, bar.next(1)[0]);
        Assertions.assertEquals(0, bar.next(0).length);
        try {
            bar.next(5);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Assertions.assertEquals("array.length=10, pos=6, length=5", e.getMessage());
        }
    }
}
