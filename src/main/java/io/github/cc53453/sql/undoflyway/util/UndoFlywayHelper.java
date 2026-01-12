package io.github.cc53453.sql.undoflyway.util;

import java.sql.Connection;
import java.sql.SQLException;

public interface UndoFlywayHelper {
    /**
     * 删除`flyway_schema_history`里当前版本的记录
     * @param version 版本号
     * @throws SQLException 更新失败抛错
     * @return 删除的行数
     */
    int delete(String version, Connection conn) throws SQLException;
}
