package io.github.cc53453.image.pojo;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 混合架构镜像
 */
@Getter
@Setter
@ToString
public class ManifestImage extends AbstractImage {
    /**
     * 该混合镜像指向的几个实际的普通镜像
     */
    private List<Image> refers;
    
    
    @Override
    public boolean isManifest() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), refers);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        
        ManifestImage other = (ManifestImage) obj;
        if (this.getRefers() == other.getRefers())
            return true;
        if (other.getRefers() == null)
            return false;
        
        return refers.size()==other.getRefers().size() && refers.containsAll(other.getRefers());
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
        if(refers == null || refers.isEmpty()) {
            return null;
        }
        Date newest = refers.get(0).getModefiedAt();
        for(int i = 1; i < refers.size(); i++) {
            if(refers.get(i).getModefiedAt().compareTo(newest) > 0) {
                newest = refers.get(i).getModefiedAt();
            }
        }
        return newest;
    }
}
