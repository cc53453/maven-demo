package io.github.cc53453.file.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件路径处理工具
 */
@Slf4j
public final class FilePathUtil {
    /**
     * 工具类，不支持实例化
     */
    private FilePathUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    private static final String CLASSPATH_PREFIX = "classpath:";
    
    /**
     * unix风格分隔符/
     */
    public static final String UNIX_SEPARATOR = "/";
    
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
    
    /**
     * 拼接路径,返回path[0]/path[1]/……，如果path[i]本身就以/结尾，则不会额外添加/
     * @param separator 分隔符
     * @param path 路径列表
     * @return 拼接后的路径
     */
    public static String concatPath(String separator, String... path) {
        if(path == null || path.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<path.length-1;i++) {
            if(path[i].endsWith(separator)) {
                sb.append(path[i]);
            }
            else {
                sb.append(path[i]).append(separator);
            }
        }
        sb.append(path[path.length-1]);
        return sb.toString();
    }
    
    /**
     * 计算相对路径
     * @param basePath 基础路径
     * @param filePath 文件路径
     * @return 文件相对基础路径的相对路径
     */
    public static String relativize(Path basePath, Path filePath) {
        return basePath.toAbsolutePath().relativize(filePath.toAbsolutePath()).toString();
    }
    
    /**
     * 路径转化为unix风格
     * @param path 路径
     * @return unix风格路径
     */
    public static String toUnixType(String path) {
        return path.replace(String.valueOf(File.separatorChar), UNIX_SEPARATOR);
    }
    
    
}
