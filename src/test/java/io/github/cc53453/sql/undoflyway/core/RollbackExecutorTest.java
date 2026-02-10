package io.github.cc53453.sql.undoflyway.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cc53453.MainWithDatasource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = MainWithDatasource.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RollbackExecutorTest {
    @Autowired
    private RollbackExecutor executer;
    @Autowired
    private DataSource dataSource;
    
    @Test
    void test() throws SQLException {
        executer.rollback("1.1.0");
        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM flyway_schema_history where version = '1.1.0'");
            ResultSet rs = ps.executeQuery()) {

           long count = -1;
           if (rs.next()) {
               count = rs.getLong(1);
           }
           Assertions.assertEquals(0, count);
        }
    }
}
