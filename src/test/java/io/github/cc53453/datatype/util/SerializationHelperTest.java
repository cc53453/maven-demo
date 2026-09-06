package io.github.cc53453.datatype.util;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class SerializationHelperTest {
    @Test
    void test() throws IOException {
    	SerializationHelper<String> helper = new SerializationHelper<>();
    	helper.serializeByJson("coaihcdoac", "test-out/serialize-test.txt");
    	Assertions.assertEquals("coaihcdoac", 
    			helper.deserializeByJson("test-out/serialize-test.txt", new TypeReference<String>() {}));
    	
    	SerializationHelper<LocalDate> helper2 = new SerializationHelper<>();
        helper2.serializeByJson(LocalDate.of(1999, 1, 1), "test-out/serialize-test2.txt");
        Assertions.assertEquals(LocalDate.of(1999, 1, 1), 
                helper2.deserializeByJson("test-out/serialize-test2.txt", new TypeReference<LocalDate>() {}));
    }

}
