package io.github.cc53453.desensitize.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpDesensitizeUtilTest {

    @Test
    public void test() {
        Assertions.assertEquals("127.*.*.1,127.*.*.2", 
                IpDesensitizeUtil.maskIpv4("127.0.0.1,127.0.0.2"));
        
    }
}
