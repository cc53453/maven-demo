package io.github.cc53453.sql.undoflyway.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface FlywaySchemaHistoryMapper {
    /**
     * 仅用于单元测试
     * @param version
     * @return
     */
    @Delete("delete from flyway_schema_history WHERE version = #{version};")
    Integer delete(String version);
}
