package io.github.cc53453.sql.undoflyway.core;

import java.sql.SQLException;
import java.util.List;

import io.github.cc53453.sql.undoflyway.mapper.FlywaySchemaHistoryMapper;
import io.github.cc53453.sql.undoflyway.mapper.UndoFlywayHistoryMapper2;
import io.github.cc53453.sql.undoflyway.model.UndoFlywayHistoryModel;
import io.github.cc53453.sql.undoflyway.util.FlywayManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.init.ScriptStatementFailedException;

import io.github.cc53453.MainWithDatasource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = MainWithDatasource.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RollbackExecutorTest {
    @Autowired
    private RollbackExecutor executer;
    @Autowired
    private FlywayManager flywayManager;
    @Autowired
    private FlywaySchemaHistoryMapper flywaySchemaHistoryMapper;
    @Autowired
    private UndoFlywayHistoryMapper2 undoFlywayHistoryMapper;
    
    /**
     * 如果因数据库状态问题，跑不过。可以直接drop掉三张表：
     * drop table demo;
     * drop table flyway_schema_history;
     * drop table undoflyway_history;
     * @throws SQLException
     */
    @Test
    void test() throws SQLException {
        flywayManager.migrate();
        
        // 回退倒数第二个版本，失败
        try {
            executer.rollbackStrict("1.0.0");
        }
        catch(IllegalStateException e) {
            log.error("{}", e.getMessage());
            Assertions.assertTrue(e.getMessage().startsWith("not the newest version"));
        }
        
        // 回退最新版本--成功
        executer.rollbackStrict("1.1.0");
        List<UndoFlywayHistoryModel> model = undoFlywayHistoryMapper.findAll();
        log.info("UndoFlywayHistoryModelList: {}", model);
        Assertions.assertEquals(1, model.size());
        
        // 再次回退最新版本--因已有成功历史，抛错
        try {
            executer.rollbackStrict("1.1.0");
        }
        catch(IllegalStateException e) {
            log.error("{}", e.getMessage());
            Assertions.assertTrue(e.getMessage().startsWith("already undo success"));
        }
        
        // 修改回退记录，使其失败，脚本的checksum变化。测试该情况下抛错
        UndoFlywayHistoryModel update = new UndoFlywayHistoryModel();
        update.setChecksum(0);
        update.setVersion("1.1.0");
        update.setSuccess(false);
        undoFlywayHistoryMapper.update(update);
        try {
            executer.rollbackStrict("1.1.0");
        }
        catch(IllegalStateException e) {
            log.error("{}", e.getMessage());
            Assertions.assertTrue(e.getMessage().startsWith("there's an undo-failed history"));
        }
        
        // 再次执行1.1.0的migrate, 并测试rollback
        flywaySchemaHistoryMapper.delete("1.1.0");
        flywayManager.migrate();
        executer.rollback("1.1.0");
        List<UndoFlywayHistoryModel> model2 = undoFlywayHistoryMapper.findAll();
        log.info("UndoFlywayHistoryModelList2: {}", model2);
        // 回退两次
        Assertions.assertEquals(2, model2.size());
        
        // 测试执行回退脚本失败的场景
        undoFlywayHistoryMapper.truncate();
        try {
            executer.rollbackStrict("1.1.0");
        }
        catch(ScriptStatementFailedException e) {
            log.error("{}", e.getMessage(), e);
            List<UndoFlywayHistoryModel> model3 = undoFlywayHistoryMapper.findAll();
            log.info("UndoFlywayHistoryModelList3: {}", model3);
            Assertions.assertFalse(model3.get(0).getSuccess());
        }

        // 把数据库状态调整成下次能幂等再跑单元测试的状态
        flywaySchemaHistoryMapper.delete("1.1.0");
        undoFlywayHistoryMapper.truncate();
    }
}
