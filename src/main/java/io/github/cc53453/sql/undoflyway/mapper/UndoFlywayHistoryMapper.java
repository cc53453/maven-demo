package io.github.cc53453.sql.undoflyway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;

import io.github.cc53453.sql.undoflyway.model.UndoFlywayHistoryModel;

@Mapper
public interface UndoFlywayHistoryMapper {
    @Select("SELECT * FROM  undoflyway_history ORDER BY installed_rank DESC;")
    List<UndoFlywayHistoryModel> findAll();
    
    @Insert("INSERT INTO undoflyway_history(version, description, script, `checksum`, installed_by, installed_on, execution_time, success, installed_rank_in_flyway_schema_history) VALUES (#{version}, #{description}, #{script}, #{checksum}, CURRENT_USER(), #{installedOn}, #{executionTime}, #{success}, #{installedRankInFlywaySchemaHistory});")
    Integer save(UndoFlywayHistoryModel model);
}
