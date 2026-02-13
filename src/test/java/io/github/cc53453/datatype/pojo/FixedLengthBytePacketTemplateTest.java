package io.github.cc53453.datatype.pojo;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.cc53453.datatype.util.ByteArrayReader;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class FixedLengthBytePacketTemplateTest {
    @Test
    void test() {
        DemoPacket1 d1 = new DemoPacket1();
        d1.setName("𪚥1");
        d1.setAge(12L);
        d1.setMoney(-2.01);
        d1.setDesc("cjdioaj21331^&(%^R$&^\n\r\n𪚥1");
        DemoPacket1 d2 = new DemoPacket1();
        d2.load(d1.toByteArray());
        log.info("d1: {}, d1.toByteArray: {}, d2: {}", 
                d1, 
                new String(d1.toByteArray(), StandardCharsets.UTF_8), 
                d2);
        Assertions.assertEquals(d1, d2);
    }
    
    @Data
    @EqualsAndHashCode(callSuper=false)
    class DemoPacket1 extends FixedLengthBytePacketTemplate{
        private String name;
        private Long age;
        private Double money;
        private String desc;
        @Override
        public byte[] toByteArray() {
            return concatByteArrays(
                    toByteArrayByRightFixed(name, StandardCharsets.UTF_8, 10, CHAR_SPACE), 
                    numberToByteArrayLeftFixedByZero(age, 5), 
                    numberToByteArrayLeftFixedByZero(money, 10), 
                    toByteArrayByLeftFixed(desc, StandardCharsets.UTF_8, 100, CHAR_SPACE));
        }
        @Override
        public void load(byte[] bytes) {
            ByteArrayReader ar = new ByteArrayReader(bytes);
            name = new String(ar.next(10), 
                    StandardCharsets.UTF_8).trim();
            age = Long.valueOf(new String(ar.next(5), 
                    StandardCharsets.UTF_8));
            money = Double.valueOf(new String(ar.next(10), 
                    StandardCharsets.UTF_8));
            desc = new String(ar.next(100), 
                    StandardCharsets.UTF_8).trim();
        }
    }
}
