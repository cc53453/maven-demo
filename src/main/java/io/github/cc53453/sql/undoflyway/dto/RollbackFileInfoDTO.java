package io.github.cc53453.sql.undoflyway.dto;

import lombok.Data;

/**
 * 回退脚本信息类
 */
@Data
public class RollbackFileInfoDTO {
    /**
     * 默认的构造函数
     */
    public RollbackFileInfoDTO() {} // NOSONAR
    
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
