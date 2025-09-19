package io.github.cc53453.sm4.listener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.cc53453.datatype.util.JacksonJsonWalker;
import io.github.cc53453.file.util.FilePathUtil;
import io.github.cc53453.file.util.YamlUtil;
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
     * @param paths 注解中获取的filesPath
     * @param environment 环境/配置变量
     * @param sm4Encryptor 加密器
     */
    public AutoEncryptLocalFile(String[] paths, Environment environment, SM4Encryptor sm4Encryptor) {
        this.sm4Encryptor = sm4Encryptor;

        filesPath = new HashSet<>();
        if(paths != null) {
            filesPath.addAll(Arrays.asList(paths));
            log.info("get filesPath: {} from annotion", filesPath);
        }
        
        // 再从配置/命令行参数里取
        String extraPaths = environment.getProperty("sm4.need2encrypt-files-path");
        if (extraPaths != null) {
            log.info("get extraPaths: {} from environment", extraPaths);
            filesPath.addAll(Arrays.asList(extraPaths.split(",")));
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
        JacksonJsonWalker.walk(json, context->{
            if(!SM4Encryptor.needEncrypted(context.getCurrentValue().asText())) {
                return;
            }

            needWrite = true;
            if(context.isArrayElement()) {
                ArrayNode arrNode = (ArrayNode)context.getParent();
                arrNode.set(Integer.valueOf(context.getCurrentKey()), sm4Encryptor.encrypt(SM4Encryptor
                        .getPlainTextWithoutPrefix(context.getCurrentValue().asText())));
            }
            else {
                ObjectNode objNode = (ObjectNode)context.getParent();
                objNode.put(context.getCurrentKey(), sm4Encryptor.encrypt(SM4Encryptor
                        .getPlainTextWithoutPrefix(context.getCurrentValue().asText())));
            }
        });
    }
}
