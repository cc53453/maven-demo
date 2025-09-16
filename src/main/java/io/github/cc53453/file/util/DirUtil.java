package io.github.cc53453.file.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 目录工具类
 */
@Slf4j
public class DirUtil {
    /**
     * 工具类，不支持实例化
     */
    private DirUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 检查目录是否存在，如果不存在则创建。相当于shell的mkdir -p
     * @param dir 目录路径
     * @return 创建成功则为true
     */
    public static boolean checkDir(String dir) {
        // 目标路径
        Path path = Paths.get(dir);

        try {
            // 创建目录，包括所有缺失的父目录
            Files.createDirectories(path);
            return true;
        } catch (IOException e) {
            log.error("Failed to create directory.", e);
            return false;
        }
    }
    
    /**
     * 列出来目录下的文件，忽略子目录
     * @param dir 目录路径
     * @return 文件名
     */
    public static List<String> listFiles(String dir) {
        List<String> result = new ArrayList<>();
        // 创建 File 对象
        File directory = new File(dir);

        // 获取目录下所有的文件和子目录
        File[] files = directory.listFiles();

        if (files == null) {
            return result;
        }
        // 遍历所有文件
        for (File file : files) {
            // 如果是文件（排除子目录）
            if (file.isFile()) {
                result.add(file.getName());
            }
        }
        return result;
    }
}
