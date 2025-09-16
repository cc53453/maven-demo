package io.github.cc53453.file.util;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * yaml读写工具
 */
@Slf4j
public class YamlUtil {
    /**
     * 工具类，不支持实例化
     */
    private YamlUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 读取 YAML 文件并返回为 json。
     * @param filePath 配置文件路径
     * @return 配置文件的内容
     */
    public static JsonNode loadYaml(String filePath) {
        JsonMapper mapper = JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT) // 美化输出
                .build();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            return mapper.valueToTree(new Yaml().load(inputStream));
        } catch (IOException e) {
            log.error("read file error: {}, {}", filePath, e.getMessage());
        }
        return mapper.nullNode();
    }

    /**
     * 将 json 写入 YML 文件。
     * @param data     数据
     * @param filePath 文件路径
     */
    public static void writeYaml(JsonNode data, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            yaml.dump(new ObjectMapper().convertValue(data, Object.class), writer);
        } catch (IOException e) {
            log.error("write file error: {}, {}, {}", filePath, data, e.getMessage());
        }
    }
}