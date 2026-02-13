package io.github.cc53453.datatype.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class NumberHelperTest {
    @Test
    void test() {
        BigDecimal bd = new BigDecimal(1);
        BigInteger bi = new BigInteger("-1234");
        Byte b = Byte.MIN_VALUE;
        Double d = -0.0213;
        Float f = -0.0f;
        Integer i = 0;
        Long l = 21L;
        Short s = Short.MIN_VALUE;
        
        Assertions.assertEquals(new BigDecimal(1), NumberHelper.abs(bd));
        Assertions.assertEquals(new BigInteger("1234"), NumberHelper.abs(bi));
        Assertions.assertEquals(Byte.MIN_VALUE, NumberHelper.abs(b));
        Assertions.assertEquals(128, NumberHelper.abs(Integer.valueOf(b)));
        Assertions.assertEquals(0.0213, NumberHelper.abs(d));
        Assertions.assertEquals(-0.0f, NumberHelper.abs(f));
        Assertions.assertEquals(0, NumberHelper.abs(i));
        Assertions.assertEquals(21L, NumberHelper.abs(l));
        Assertions.assertEquals(Short.MIN_VALUE, NumberHelper.abs(s));
        Assertions.assertEquals(32768, NumberHelper.abs(Integer.valueOf(s)));

        Assertions.assertFalse(NumberHelper.isNegative(bd));
        Assertions.assertTrue(NumberHelper.isNegative(bi));
        Assertions.assertTrue(NumberHelper.isNegative(b));
        Assertions.assertTrue(NumberHelper.isNegative(d));
        Assertions.assertFalse(NumberHelper.isNegative(f));
        Assertions.assertFalse(NumberHelper.isNegative(i));
        Assertions.assertFalse(NumberHelper.isNegative(l));
        Assertions.assertTrue(NumberHelper.isNegative(s));
    }
}
