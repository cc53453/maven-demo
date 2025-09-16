package io.github.cc53453.file.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * properties文件读写工具
 */
@Slf4j
public class PropertiesUtil {
    /**
     * 工具类，不支持实例化
     */
    private PropertiesUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 读取properties文件
     * @param filePath 文件路径
     * @return 配置文件内容
     */
    public static Properties loadProperties(String filePath) {
        Properties props = new Properties();

        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8.name())) {
            props.load(reader);
        } catch (IOException e) {
            log.error("read file error: {}, {}", filePath, e.getMessage());
        }
        
        return props;
    }
    
    /**
     * 写入properties文件
     * @param filePath 文件路径
     * @param props 配置文件内容
     * @param comments 注释，会生成在文件开头 
     */
    public static void writeProperties(String filePath, Properties props, String comments) {
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8)) {
            props.store(writer, comments);
        } catch (IOException e) {
            log.error("write file error: {}, {}, {}, {}", filePath, props, comments, e.getMessage());
        }
    }
    
}
