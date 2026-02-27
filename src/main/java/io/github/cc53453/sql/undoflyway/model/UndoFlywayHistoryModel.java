package io.github.cc53453.sql.undoflyway.model;

import java.util.Date;

import lombok.Data;

/**
 * 数据库模型类
 */
@Data
public class UndoFlywayHistoryModel {
    /**
     * 默认的构造函数
     */
    public UndoFlywayHistoryModel() {} // NOSONAR
    
    /**
     * 自增主键
     */
    private Long installedRank;
    /**
     * 版本
     */
    private String version;
    /**
     * 描述，一般是文件名去掉Vxx__和.sql后，把下划线替换为空格
     */
    private String description;
    /**
     * 执行的回退脚本
     */
    private String script;
    /**
     * 脚本校验和
     */
    private Integer checksum;
    /**
     * 链接数据库的用户
     */
    private String installedBy;
    /**
     * 什么时候回退的
     */
    private Date installedOn;
    /**
     * 脚本执行了多久(ms)
     */
    private Long executionTime;
    /**
     * 脚本是否执行成功
     */
    private Boolean success;
    /**
     * 在FlywaySchemaHistory表中的主键，表示针对的是那一次sql变更的回退
     */
    private Integer installedRankInFlywaySchemaHistory;
}