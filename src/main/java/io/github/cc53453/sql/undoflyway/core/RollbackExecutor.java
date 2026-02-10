package io.github.cc53453.sql.undoflyway.core;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import io.github.cc53453.file.util.DirUtil;
import io.github.cc53453.file.util.FilePathUtil;
import io.github.cc53453.sql.undoflyway.config.UndoFlywayConfig;
import io.github.cc53453.sql.undoflyway.enums.DatasourceType;
import io.github.cc53453.sql.undoflyway.util.UndoFlywayHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * 回退工具
 */
@Component
@Slf4j
public class RollbackExecutor {
    private final DataSource dataSource;
    private final UndoFlywayConfig undoFlywayConfig;

    public RollbackExecutor(DataSource dataSource, UndoFlywayConfig undoFlywayConfig) {
        this.dataSource = dataSource;
        this.undoFlywayConfig = undoFlywayConfig;
    }

    /**
     * 根据版本号，执行${spring.undoflyway.locations}/V${version}__*.sql，并删除flyway_schema_history中该version的记录
     * @param version 版本号
     * @throws SQLException sql执行异常
     */
    public void rollback(String version) throws SQLException {
        String path = FilePathUtil.getFullPathByClassPathResource(undoFlywayConfig.getLocations());
        List<File> files = DirUtil.listFiles(path);
        
        String filenameStartWith = String.format("V%s__", version);
        for(File f:files) {
            if(f.getName().startsWith(filenameStartWith) && 
                    f.getName().endsWith(".sql")) {
                log.info("executeSqlScript: {}", f.getAbsolutePath());
                try (Connection conn = dataSource.getConnection()) {
                    conn.setAutoCommit(false);
                    ScriptUtils.executeSqlScript(
                            conn,
                            new FileSystemResource(f.getAbsolutePath())
                    );
                    UndoFlywayHelper helper = getHelper();
                    int cnt = helper.delete(version, conn);
                    log.info("delete {} in flyway_schema_history, cnt: {}", version, cnt);
                    conn.commit();
                }
                // 理论上不允许有多个相同版本号的sql文件，只会执行第一个
                return ;
            }
        }
    }
    
    private UndoFlywayHelper getHelper() {
        if(DatasourceType.MYSQL.equals(undoFlywayConfig.getDatasourceType())) {
            return new io.github.cc53453.sql.mysql.util.UndoFlywayHelperImpl();
        }
        throw new java.lang.IllegalArgumentException("unsupported database type");
    }
}
