package io.github.cc53453.sql.undoflyway.util;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

import org.flywaydb.core.internal.resolver.ChecksumCalculator;
import org.flywaydb.core.internal.resource.filesystem.FileSystemResource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.stereotype.Component;

import io.github.cc53453.sql.undoflyway.dto.RollbackFileInfoDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * flyway api工具
 */
@Component
@Slf4j
public class FlywayManager {
    /**
     * flyway api
     */
    private final Flyway flyway;
    
    /**
     * 构造函数
     * @param flyway bean注入
     */
    public FlywayManager(Flyway flyway) {
        this.flyway = flyway;
    }
    
    /**
     * 获取最新版本
     * @return 版本的字符串
     */
    public String getLatestVersion() {
        MigrationInfo current = flyway.info().current();

        if (current == null) {
            throw new NoSuchElementException("no version in flyway");
        }
        log.debug("current migrationInfo: {}", current);
        return current.getVersion().getVersion();
    }
    
    /**
     * 返回指定版本的InstalledRank
     * @param version 版本
     * @return InstalledRank
     */
    public Integer getInstalledRankByVersion(String version) {
        if(version == null) {
            throw new IllegalArgumentException("version should not be null");
        }
        for (MigrationInfo info : flyway.info().all()) {
            if (version.equals(info.getVersion().getVersion())) {
                log.debug("migrationInfo: {}", info);
                return info.getInstalledRank();
            }
        }
        throw new NoSuchElementException("no such version in flyway, ".concat(version));
    }
    
    /**
     * 手工触发migrate
     * @return 结果
     */
    public MigrateResult migrate() {
        return flyway.migrate();
    }
    
    /**
     * 计算文件的校验和。注意这里调用的是flyway的工具类，可能和flyway版本有依赖
     * @param file sql文件
     * @return 校验和
     */
    public static Integer calculateChecksum(RollbackFileInfoDTO file) {
        String fileName = file.getFullFilename();
        int checksum = ChecksumCalculator.calculate(new FileSystemResource(
                null, fileName, StandardCharsets.UTF_8, false));
        file.setChecksum(checksum);
        return checksum;
    }
}
