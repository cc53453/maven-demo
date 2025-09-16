package io.github.cc53453.sm4.listener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.cc53453.file.util.FilePathUtil;
import io.github.cc53453.file.util.YamlUtil;
import io.github.cc53453.sm4.annotation.EnableSm4Encrypt;
import io.github.cc53453.sm4.core.SM4Encryptor;
import lombok.extern.slf4j.Slf4j;

/**
 * 启动时自动加密需要加密的字段
 */
@Slf4j
public class AutoEncryptLocalFile implements CommandLineRunner {
    private final SM4Encryptor sm4Encryptor;
    private Set<String> filesPath;
    private boolean needWrite = false;

    /**
     * 获取注解中的filesPath，作为后续需要加密的文件
     * @param applicationContext spring容器上下文
     * @param sm4Encryptor 加密器
     */
    public AutoEncryptLocalFile(ApplicationContext applicationContext, SM4Encryptor sm4Encryptor) {
        this.sm4Encryptor = sm4Encryptor;

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(EnableSm4Encrypt.class);

        filesPath = new HashSet<>();
        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            String beanName = entry.getKey();
            Object bean = entry.getValue();
            List<String> paths = Arrays.asList(bean.getClass().getAnnotation(
                    EnableSm4Encrypt.class).filesPath());
            log.info("get filesPath: {} from class: {}", paths, beanName);
            filesPath.addAll(paths);
        }
    }
    
    /**
     * 获取主函数上的注解EnableSm4Encrypt中的filesPath，遍历加密这些文件
     */
    @Override
    public void run(String... args) throws Exception {
        for (String path : filesPath) {
            if(FilePathUtil.isClassPathResource(path)) {
                path = FilePathUtil.getFullPathByClassPathResource(path);
            }
            needWrite = false;
            JsonNode json = YamlUtil.loadYaml(path);
            encrypt(json);
            if(needWrite) {
                YamlUtil.writeYaml(json, path);
                log.warn("encrypt file: {}", path);
            }
        }
    }
    
    private void encrypt(JsonNode json) {
        if (json.isObject()) {
            ObjectNode objNode = (ObjectNode) json;
            objNode.fields().forEachRemaining(entry -> {
                JsonNode child = entry.getValue();
                if (child.isTextual() && SM4Encryptor.
                        needEncrypted(String.valueOf(child.asText()))) {
                    needWrite = true;
                    objNode.put(entry.getKey(), sm4Encryptor.encrypt(SM4Encryptor
                            .getPlainTextWithoutPrefix(String.valueOf(child.asText()))));
                } else {
                    encrypt(child);
                }
            });
        } 
        else if (json.isArray()) {
            ArrayNode arrNode = (ArrayNode) json;
            for (int i = 0; i < arrNode.size(); i++) {
                JsonNode child = arrNode.get(i);
                if (child.isTextual() && SM4Encryptor.
                        needEncrypted(String.valueOf(child.asText()))) {
                    needWrite = true;
                    arrNode.set(i, sm4Encryptor.encrypt(SM4Encryptor
                            .getPlainTextWithoutPrefix(String.valueOf(child.asText()))));
                } else {
                    encrypt(child);
                }
            }
        }
    }
}
