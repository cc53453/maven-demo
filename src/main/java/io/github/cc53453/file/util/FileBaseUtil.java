package io.github.cc53453.file.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
}
