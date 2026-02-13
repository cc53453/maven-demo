package io.github.cc53453.sql.undoflyway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import io.github.cc53453.sql.undoflyway.model.UndoFlywayHistoryModel;

@Mapper
public interface UndoFlywayHistoryMapper2 {
    @Select("SELECT * FROM  undoflyway_history ORDER BY installed_rank DESC;")
    List<UndoFlywayHistoryModel> findAll();
    
    @Insert("INSERT INTO undoflyway_history(version, description, script, `checksum`, installed_by, installed_on, execution_time, success, installed_rank_in_flyway_schema_history) VALUES (#{version}, #{description}, #{script}, #{checksum}, CURRENT_USER(), #{installedOn}, #{executionTime}, #{success}, #{installedRankInFlywaySchemaHistory});")
    Integer save(UndoFlywayHistoryModel model);
    
    /**
     * 因单元测试需要，额外提供。不对用户提供
     * @param model
     * @return
     */
    @Update("UPDATE undoflyway_history set `checksum`=#{checksum}, success=#{success} WHERE version = #{version}")
    Integer update(UndoFlywayHistoryModel model);
    
    /**
     * 因单元测试需要，额外提供。不对用户提供
     * @return
     */
    @Delete("TRUNCATE table undoflyway_history;")
    Integer truncate();
}
