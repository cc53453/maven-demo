package io.github.cc53453.sql.undoflyway.core;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import io.github.cc53453.file.util.DirUtil;
import io.github.cc53453.file.util.FilePathUtil;
import io.github.cc53453.sql.undoflyway.config.UndoFlywayConfig;
import io.github.cc53453.sql.undoflyway.dto.RollbackFileInfoDTO;
import io.github.cc53453.sql.undoflyway.mapper.UndoFlywayHistoryMapper;
import io.github.cc53453.sql.undoflyway.model.UndoFlywayHistoryModel;
import io.github.cc53453.sql.undoflyway.util.FlywayManager;
import lombok.extern.slf4j.Slf4j;

/**
 * 回退工具
 */
@Component
@Slf4j
public class RollbackExecutor {
    private final DataSource dataSource;
    private final FlywayManager flyway;
    private final UndoFlywayHistoryMapper undoFlywayHistoryMapper;
    private final UndoFlywayConfig undoFlywayConfig;

    /**
     * 构造函数
     * @param dataSource spring注入
     * @param flyway spring注入
     * @param undoFlywayHistoryMapper spring注入
     * @param undoFlywayConfig spring注入
     */
    public RollbackExecutor(DataSource dataSource, FlywayManager flyway, UndoFlywayHistoryMapper undoFlywayHistoryMapper, UndoFlywayConfig undoFlywayConfig) {
        this.dataSource = dataSource;
        this.flyway = flyway;
        this.undoFlywayHistoryMapper = undoFlywayHistoryMapper;
        this.undoFlywayConfig = undoFlywayConfig;
    }

    /**
     * 严格管控回退。更建议用户调用本方法。
     * 会进行如下检查：1. 已经回退过且success的版本不再重复 2. 历史回退过但失败的版本，如果脚本的checksum变了也不允许再执行 3. 只允许回退最新的版本
     * @param version 回退哪个版本
     * @throws SQLException sql抛错
     */
    public void rollbackStrict(String version) throws SQLException {
        String newest = flyway.getLatestVersion();
        if(!newest.equals(version)) {
            throw new IllegalStateException(String.format(
                    "not the newest version, toUndo: %s, newest: %s", version, newest));
        }
        
        List<UndoFlywayHistoryModel> history = undoFlywayHistoryMapper.findAll();
        for(UndoFlywayHistoryModel undo:history) {
            if(version.equals(undo.getVersion())) {
                // 过去成功过
                if(Boolean.TRUE.equals(undo.getSuccess())) {
                    throw new IllegalStateException("already undo success. cannot undo again! undo histroy:".concat(undo.toString()));
                }
                
                // 虽然过去失败了，但是过去的脚本的checksum和本次不一样了
                RollbackFileInfoDTO file = getScript(version);
                if(!undo.getChecksum().equals(file.getChecksum())) {
                    throw new IllegalStateException(String.format(
                            "there's an undo-failed history, but history.checksum != current.checksum. history: %s, current: %s", 
                            undo, file));
                }
            }
        }

        rollback(version);
    }
    
    
    /**
     * 根据版本号，执行${spring.undoflyway.locations}/V${version}__*.sql，并在undoflyway_history中新增回退记录
     * 注意！要调用本方法，必须确保数据库中有undoflyway_history表。该表的新增也可以用flyway管理，样例见src/test/resources/db/migrate/V0.0.1__create_undoflyway_history.sql
     * @param version 版本号
     * @throws SQLException sql执行异常
     */
    public void rollback(String version) throws SQLException {
        RollbackFileInfoDTO file = getScript(version);
        log.info("executeSqlScript: {}", file);
        
        UndoFlywayHistoryModel model = new UndoFlywayHistoryModel();
        model.setVersion(version);
        model.setDescription(file.getDescription());
        model.setScript(file.getFilename());
        model.setChecksum(file.getChecksum());
        model.setInstalledOn(new Date());
        model.setExecutionTime(-1L);
        model.setSuccess(false);
        model.setInstalledRankInFlywaySchemaHistory(flyway.getInstalledRankByVersion(version));
        
        Instant start = Instant.now();
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            ScriptUtils.executeSqlScript(
                    conn,
                    new FileSystemResource(file.getFullFilename())
            );
            conn.commit();
            model.setSuccess(true);
        }
        finally {
            Instant end = Instant.now();
            model.setExecutionTime(Duration.between(start, end).toMillis());
            undoFlywayHistoryMapper.save(model);
        }
    }
    
    private RollbackFileInfoDTO getScript(String version) {
        String path = FilePathUtil.getFullPathByClassPathResource(undoFlywayConfig.getLocations());
        List<File> files = DirUtil.listFiles(path);
        
        String filenameStartWith = String.format("V%s__", version);
        for(File f:files) {
            if(f.getName().startsWith(filenameStartWith) && 
                    f.getName().endsWith(".sql")) {
                log.debug("find rollback sql: {}", f.getAbsolutePath());
                RollbackFileInfoDTO script = new RollbackFileInfoDTO();
                script.setFullFilename(f.getAbsolutePath());
                script.setFilename(f.getName());
                script.setDir(f.getParent());
                // 去掉版本号前缀
                String description = f.getName().substring(filenameStartWith.length());
                // 去掉.sql后缀，把_替换成空格
                description = description.substring(0, description.length() - 4).replace("_"," ");
                script.setDescription(description);
                FlywayManager.calculateChecksum(script);
                return script;
            }
        }
        throw new NoSuchElementException("find no rollback sql for version: ".concat(version));
    }
}
