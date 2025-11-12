package io.github.cc53453.sql.mysql.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.github.cc53453.date.util.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MysqlHelperTest {
    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS);
        TestEntity test1 = new TestEntity();
        test1.setId(1L);
        test1.setName("test1");
        test1.setIsStaff(false);
        test1.setSex('1');
        test1.setCreateDate(new Date());
        String sql1 = MysqlHelper.mybatisEntityToInsertSql(test1, "db1.test", TestEntity.class, null);
        log.info("sql1: {}", sql1);
        Assertions.assertTrue(sql1.contains(String.valueOf(test1.getId())));
        Assertions.assertTrue(sql1.contains(String.valueOf(test1.getName())));
        Assertions.assertTrue(sql1.contains("is_staff"));
        Assertions.assertTrue(sql1.contains(String.valueOf(test1.getSex())));
        Assertions.assertTrue(sql1.contains(sdf.format(test1.getCreateDate())));
        
        TestEntity test2 = new TestEntity();
        test2.setId(2L);
        test2.setName("test2");
        String sql2 = MysqlHelper.mybatisEntityToInsertSql(test2, "db1.test", TestEntity.class, null);
        log.info("sql2: {}", sql2);
        Assertions.assertTrue(sql2.contains(String.valueOf(test2.getId())));
        Assertions.assertTrue(sql2.contains(String.valueOf(test2.getName())));
        Assertions.assertFalse(sql2.contains("is_staff"));
        Assertions.assertFalse(sql2.contains(String.valueOf(test2.getSex())));
        Assertions.assertFalse(sql2.contains("create_date"));
    }
    
    @Data
    public class TestEntity {
        @TableId(type = IdType.AUTO)
        @TableField("id")
        private Long id;
        @TableField("name")
        private String name;
        @TableField("is_staff")
        private Boolean isStaff;
        @TableField("sex")
        private Character sex;
        @TableField("create_date")
        private Date createDate;
    }
}
