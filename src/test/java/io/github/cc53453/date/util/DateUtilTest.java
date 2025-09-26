package io.github.cc53453.date.util;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtilTest {
    @Test
    public void test() {
        LocalDate day = DateUtil.toLocalDate("2023-06-21T07:19:49.121+08:00", 
                DateUtil.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSXXX);
        Assertions.assertEquals(2023, day.getYear());
        Assertions.assertEquals(6, day.getMonth().getValue());
        Assertions.assertEquals(21, day.getDayOfMonth());
    }

}
