package io.github.cc53453.sql.mysql.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import io.github.cc53453.sql.mysql.annotation.ValidateTableName;

/**
 * 通用的一些mapper方法
 */
@Mapper
public interface CommonMapper {
	/**
	 * 清空表
	 * @param tableName 表名
	 */
	@Update("TRUNCATE TABLE ${tableName}")
	@ValidateTableName
	public void truncateTable(String tableName);
	
	/**
	 * 从表Acopy结构到表B
	 * @param sourceTableName 源表名
	 * @param targetTableName 目标表名
	 */
	@Update("CREATE TABLE ${targetTableName} LIKE ${sourceTableName}")
	@ValidateTableName
	public void copyTableStructure(String sourceTableName, String targetTableName);
	
	/**
	 * 从表Acopy数据到表B
	 * @param sourceTableName 源表名
	 * @param targetTableName 目标表名
	 * @return 影响行数
	 */
	@Insert("INSERT INTO ${targetTableName} SELECT * FROM ${sourceTableName}")
	@ValidateTableName
	public int copyTableData(String sourceTableName, String targetTableName);

	/**
	 * IF EXISTS删除表
	 * @param tableName 表名
	 */
	@Update("DROP TABLE IF EXISTS ${tableName}")
	@ValidateTableName
	public void dropTable(String tableName);
	
	/**
	 * 重命名表
	 * @param oldTableName 旧表名
	 * @param newTableName 新表名
	 */
	@Update("RENAME TABLE ${oldTableName} TO ${newTableName}")
	@ValidateTableName
	public void renameTable(String oldTableName, String newTableName);
}
