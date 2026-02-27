package io.github.cc53453.sql.undoflyway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;

import io.github.cc53453.sql.undoflyway.model.UndoFlywayHistoryModel;

/**
 * 回退审计表mapper
 */
@Mapper
public interface UndoFlywayHistoryMapper {
    /**
     * 搜索所有记录，以installed_rank倒序排序
     * @return 回退记录的列表
     */
    @Select("SELECT * FROM  undoflyway_history ORDER BY installed_rank DESC;")
    List<UndoFlywayHistoryModel> findAll();
    
    /**
     * 插入一条新纪录
     * @param model 模型类
     * @return 影响的行数
     */
    @Insert("INSERT INTO undoflyway_history(version, description, script, `checksum`, installed_by, installed_on, execution_time, success, installed_rank_in_flyway_schema_history) VALUES (#{version}, #{description}, #{script}, #{checksum}, CURRENT_USER(), #{installedOn}, #{executionTime}, #{success}, #{installedRankInFlywaySchemaHistory});")
    Integer save(UndoFlywayHistoryModel model);
}
