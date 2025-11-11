package io.github.cc53453.image.pojo;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 普通镜像，非混合架构镜像
 */
@Getter
@Setter
@ToString
public class Image extends AbstractImage {
    /**
     * 镜像id，唯一标识
     */
    private String id;
    /**
     * 镜像层
     */
    private List<String> layers;
    /**
     * cpu架构
     */
    private String architecture;
    /**
     * 操作系统
     */
    private String os;
    /**
     * 何时创建
     */
    private Date createAt;
    /**
     * 何时修改
     */
    private Date modefiedAt;
    
    @Override
    public boolean isManifest() {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), architecture, id, layers, os);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Image other = (Image) obj;
        return id.equals(other.getId());
    }

    /**
     * 根据最新修改时间排序
     */
    @Override
    public int compareTo(AbstractImage o) {
        return this.latestUpdateDateTime().compareTo(o.latestUpdateDateTime());
    }

    @Override
    public Date latestUpdateDateTime() {
        return modefiedAt;
    }
}
