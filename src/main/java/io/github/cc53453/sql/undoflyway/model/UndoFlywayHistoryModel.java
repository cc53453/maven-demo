package io.github.cc53453.sql.undoflyway.model;

import java.util.Date;

import lombok.Data;

@Data
public class UndoFlywayHistoryModel {
    private Long installedRank;
    private String version;
    private String description;
    private String script;
    private Integer checksum;
    private String installedBy;
    private Date installedOn;
    private Long executionTime;
    private Boolean success;
    /**
     * 在FlywaySchemaHistory表中的主键，表示针对的是那一次sql变更的回退
     */
    private Integer installedRankInFlywaySchemaHistory;
}