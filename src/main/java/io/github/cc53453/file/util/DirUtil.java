package io.github.cc53453.file.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 目录工具类
 */
@Slf4j
public final class DirUtil {
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
     * @return 文件名, 不带目录路径信息
     */
    public static List<File> listFiles(String dir) {
        List<File> result = new ArrayList<>();
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
                result.add(file);
            }
        }
        return result;
    }
    
    /**
     * 递归列出目录下的所有文件（包含子目录）。相当于shell的find /xxx/ -type f
     * @param dir 目录路径
     * @return 文件File对象
     */
    public static List<File> listFilesRecursive(String dir) {
        List<File> result = new ArrayList<>();
        File directory = new File(dir);

        if (!directory.exists() || !directory.isDirectory()) {
            return result;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return result;
        }

        for (File file : files) {
            if (file.isFile()) {
                // 加绝对路径，避免重名文件难区分
                result.add(file);
            } else if (file.isDirectory()) {
                // 递归子目录
                result.addAll(listFilesRecursive(file.getAbsolutePath()));
            }
        }
        return result;
    }

    
    /**
     * 删除整个目录，相当于 rm -rf path
     * @param dir 要删除的目录/文件路径
     */
    public static void deleteRecursive(String dir) {
        Path path = Paths.get(dir);
        if (!Files.exists(path)) {
            return;
        }

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file); // 删除文件
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir); // 目录最后删除
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log.error("rm -rf failed, {}", dir, e);
        }
    }
}
