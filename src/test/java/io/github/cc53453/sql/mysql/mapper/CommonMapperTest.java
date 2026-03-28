package io.github.cc53453.sql.mysql.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cc53453.MainWithDatasource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = MainWithDatasource.class)
class CommonMapperTest {
	@Autowired
	private CommonMapper commonMapper;
	
	@Test
	void test() {
		commonMapper.copyTableStructure("demo", "demo_copy");
		Assertions.assertTrue(
				commonMapper.copyTableData("demo", "demo_copy") > 0
				);
		commonMapper.renameTable("demo_copy", "demo_copy_renamed");
		commonMapper.truncateTable("demo_copy_renamed");
		
		try {
			commonMapper.dropTable("demo_copy_renamed; DROP TABLE demo");
		} catch (Exception e) {
			log.error("删除表失败", e);
			Assertions.assertTrue(e instanceof SecurityException);
		}
		commonMapper.dropTable("demo_copy_renamed");
	}
}
