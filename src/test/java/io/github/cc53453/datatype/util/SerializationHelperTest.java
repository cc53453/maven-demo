package io.github.cc53453.datatype.util;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class SerializationHelperTest {
    @Test
    void test() throws IOException {
    	SerializationHelper<String> helper = new SerializationHelper<>();
    	helper.serializeByJson("coaihcdoac", "test-out/serialize-test.txt");
    	Assertions.assertEquals("coaihcdoac", 
    			helper.deserializeByJson("test-out/serialize-test.txt"));
    }

}
