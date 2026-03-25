package io.github.cc53453.sql.undoflyway.util;

import java.sql.SQLException;

import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cc53453.MainWithDatasource;
import io.github.cc53453.file.util.FilePathUtil;
import io.github.cc53453.sql.undoflyway.core.RollbackExecutor;
import io.github.cc53453.sql.undoflyway.dto.RollbackFileInfoDTO;
import io.github.cc53453.sql.undoflyway.mapper.FlywaySchemaHistoryMapper;
import io.github.cc53453.sql.undoflyway.mapper.UndoFlywayHistoryMapper2;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = MainWithDatasource.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FlywayManagerTest {
    @Autowired
    private RollbackExecutor executer;
    @Autowired
    private FlywayManager flywayManager;
    @Autowired
    private FlywaySchemaHistoryMapper flywaySchemaHistoryMapper;
    @Autowired
    private UndoFlywayHistoryMapper2 undoFlywayHistoryMapper;
    
    @Test
    void test() throws SQLException {
        RollbackFileInfoDTO f = new RollbackFileInfoDTO();
        f.setFullFilename(FilePathUtil.getFullPathByClassPathResource("classpath:db/rollback/V1.1.0__alter_demo_drop_age.sql"));
        Integer i = FlywayManager.calculateChecksum(f);
        Assertions.assertEquals(f.getChecksum(), i);
        log.info("file: {}", f);
        
        MigrateResult result = flywayManager.migrate();
        log.info("migrate result: {}", result);
        
        Assertions.assertEquals("1.1.0", flywayManager.getLatestVersion());
        Assertions.assertEquals(4, 
                flywayManager.getInstalledRankByVersion("1.1.0"));

        // 把数据库状态调整成下次能幂等再跑单元测试的状态
        executer.rollback("1.1.0");
        flywaySchemaHistoryMapper.delete("1.1.0");
        undoFlywayHistoryMapper.truncate();
    }
}
