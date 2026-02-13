package io.github.cc53453.datatype.util;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class DateHelperTest {
    @Test
    void test() {
        LocalDate day = DateHelper.toLocalDate("2023-06-21T07:19:49.121+08:00", 
                DateHelper.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSXXX);
        Assertions.assertEquals(2023, day.getYear());
        Assertions.assertEquals(6, day.getMonth().getValue());
        Assertions.assertEquals(21, day.getDayOfMonth());
        
        Assertions.assertTrue(DateHelper.now(DateHelper.FORMAT_YYYYMMDD).matches("\\d{8}"));
        Assertions.assertEquals(-1, DateHelper.daysBetween("20260202", "20260201", DateHelper.FORMAT_YYYYMMDD));
        Assertions.assertEquals(1, DateHelper.daysBetween("20260201", "20260202", DateHelper.FORMAT_YYYYMMDD));
    }
}
