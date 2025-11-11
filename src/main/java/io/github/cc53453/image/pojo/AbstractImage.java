package io.github.cc53453.image.pojo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 最基本的镜像描述字段和行为，无论单架构镜像还是混合架构镜像都一定要有这些字段和行为 
 */
@Getter
@Setter
@ToString
public abstract class AbstractImage implements Comparable<AbstractImage> {
    /**
     * 来自哪个仓库
     */
    private String repo;
    /**
     * 镜像名称
     */
    private String imageName;
    /**
     * 镜像Tag
     */
    private String imageTag;
    /**
     * 是否混合架构的镜像
     */
    public abstract boolean isManifest();
    /**
     * 返回最新一次修改的时间。镜像比较顺序时会按最近修改时间排序
     * @return Date时间
     */
    public abstract Date latestUpdateDateTime();
}
