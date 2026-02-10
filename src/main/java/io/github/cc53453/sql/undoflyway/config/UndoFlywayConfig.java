package io.github.cc53453.sql.undoflyway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import io.github.cc53453.sql.undoflyway.enums.DatasourceType;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * flyway回退配置类，需要定义回退脚本路径等
 */
@Configuration
@ConfigurationProperties(prefix = "spring.undoflyway")
@Validated
@Data
public class UndoFlywayConfig {
    /**
     * 默认构造函数，由 Spring 自动调用
     */
    public UndoFlywayConfig() {} //NOSONAR

    /**
     * 回退脚本的类路径
     */
    @Pattern(regexp = "^classpath:.*$", message = "路径格式必须为类路径")
    private String locations = "classpath:db/rollback";
    
    /**
     * 数据源类型,详见{@link io.github.cc53453.sql.undoflyway.enums.DatasourceType}
     */
    private DatasourceType datasourceType = DatasourceType.MYSQL;
}
