package io.github.cc53453.datatype.util;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ClassReflectHelperTest {
    @Test
    void test() {
        Demo d = new Demo();
        for (Field field : Demo.class.getDeclaredFields()) {
            try {
                Object o = ClassReflectHelper.getFieldValue(d, field);
                Assertions.assertEquals(d.getField1(), o);
            }
            catch(IllegalArgumentException e) {
                Assertions.assertTrue(e.getMessage().startsWith("Failed to get value for field"));
                log.error("expected error", e);
            }
        }
    }
    
    
    public class Demo {
        @Getter
        private String field1;
        @Setter
        private String field2;
    }
}
