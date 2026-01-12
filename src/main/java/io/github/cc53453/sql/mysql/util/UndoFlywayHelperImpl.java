package io.github.cc53453.sql.mysql.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.github.cc53453.sql.undoflyway.util.UndoFlywayHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * UndoFlyway回退变更记录的助手（Mysql版）
 */
@Slf4j
public class UndoFlywayHelperImpl implements UndoFlywayHelper {
    /**
     * delete `flyway_schema_history` where `version` = "${version}";
     */
    @Override
    public int delete(String version, Connection conn) throws SQLException {
        String sql = "DELETE FROM flyway_schema_history WHERE version = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, version);
            int rows = ps.executeUpdate();
            log.debug("Deleted rows: " + rows);
            return rows;
        }
    }
}
