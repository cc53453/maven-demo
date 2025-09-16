package io.github.cc53453.file.util;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件路径处理工具
 */
@Slf4j
public class FilePathUtil {
    /**
     * 工具类，不支持实例化
     */
    private FilePathUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    private final static String CLASSPATH_PREFIX = "classpath:";
    
    /**
     * 判断path是否类路径形式
     * @param path 文件路径
     * @return 如果以{@link #CLASSPATH_PREFIX}打头则返回true
     */
    public static boolean isClassPathResource(String path) {
        return path!=null && path.startsWith(CLASSPATH_PREFIX);
    }
    
    /**
     * 根据类路径获取文件完整路径
     * @param path 类路径
     * @return 失败会返回输入的path
     */
    public static String getFullPathByClassPathResource(String path) {
        Resource resource = new ClassPathResource(path.substring(CLASSPATH_PREFIX.length()));
        try {
            return resource.getURI().getPath();
        } catch (IOException e) {
            log.error("getFullPathByClassPathResource failed: {}, {}", path, e.getMessage());
            return path;
        }
    }
}
