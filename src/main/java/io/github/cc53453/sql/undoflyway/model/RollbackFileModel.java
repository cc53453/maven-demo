package io.github.cc53453.sql.undoflyway.model;

import lombok.Data;

@Data
public class RollbackFileModel {
    /**
     * 所在目录
     */
    private String dir;
    /**
     * 文件名，不带路径
     */
    private String filename;
    /**
     * 带路径的文件
     */
    private String fullFilename;
    /**
     * 校验和
     */
    private Integer checksum;
    /**
     * 描述。一般是文件名去掉Vversion__后把下划线替换成空格的名字
     */
    private String description;
}
