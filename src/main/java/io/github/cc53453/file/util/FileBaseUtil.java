package io.github.cc53453.file.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件通用工具
 */
@Slf4j
public final class FileBaseUtil {
    /**
     * 工具类，不支持实例化
     */
    private FileBaseUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 判断文件或目录是否存在
     * @param filePath 文件路径
     * @return 存在返回true
     */
    public static boolean exists(String filePath) {
        return new File(filePath).exists();
    }
    
    /**
     * 计算文件行数。相当于shell的cat xxx| wc -l
     * @param filePath 文件路径
     * @return 文件行数
     */
    public static Long fileLineCount(String filePath) {
        try {
            long lineCount = Files.lines(Paths.get(filePath)).count();  // 使用流式处理并计数
            return lineCount;
        } catch (IOException e) {
            log.error("fileLineCount failed: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 删除文件。相当于shell的rm -f xx
     * @param filePath 文件路径
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);

        if (file.delete()) {
            log.info("delete {} success", filePath);
        } else {
            log.error("delete {} error, maybe it's not exist, or cannot be deleted", filePath);
        }
    }
    
    /**
     * 复制文件，如果目标文件已存在会覆盖。相当于shell的\cp src tgt
     * @param src 源文件
     * @param tgt 目标文件
     */
    public static void copyFile(String src, String tgt) {
        Path source = Paths.get(src);
        Path target = Paths.get(tgt);
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("copy {} to {} error, maybe src not exist", src, tgt);
        }
    }
}
