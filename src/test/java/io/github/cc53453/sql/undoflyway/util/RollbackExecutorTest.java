package io.github.cc53453.sql.undoflyway.util;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cc53453.Main;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RollbackExecutorTest {
    @Autowired
    private RollbackExecutor executer;
    
    @Test
    void test() throws SQLException {
        executer.rollback("1.1.0");
    }
}
