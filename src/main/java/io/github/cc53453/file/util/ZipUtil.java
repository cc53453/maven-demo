package io.github.cc53453.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import lombok.extern.slf4j.Slf4j;

/**
 * zip包工具类
 */
@Slf4j
public final class ZipUtil {
    /**
     * 文件后缀
     */
    public static final String fileExtension = ".zip";
    /**
     * 工具类，不支持实例化
     */
    private ZipUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 通过文件名判断是否为properties文件
     * @param fileName 文件名
     * @return 是返回true
     */
    public static boolean isZip(String fileName) {
        if(fileName == null) {
            return false;
        }
        return fileName.toLowerCase().endsWith(fileExtension);
    }
    
    /**
     * 解压zip文件
     * @param zipFilePath zip文件路径
     * @param destDir 解压到的目标目录
     */
    public static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            byte[] buffer = new byte[4096];
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    // 确保父目录存在
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        } catch (IOException e) {
            log.error("unzip failed: {}, {}, {}", zipFilePath, destDir, e.getMessage());
        }
    }
    
    /**
     * 压缩文件或目录
     * @param sourcePath 源文件/目录路径
     * @param zipFilePath zip包路径
     * @param baseDirPath 基准目录
     */
    public static void zip(String sourcePath, String zipFilePath, String baseDirPath) {
        File sourceFile = new File(sourcePath);
        File baseDir = new File(baseDirPath);

        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            compress(sourceFile, zos, baseDir);
        } catch (IOException e) {
            log.error("zip failed: {}, {}, {}, {}", sourcePath, zipFilePath, baseDirPath, e.getMessage());
        }
    }

    /**
     * 递归压缩
     * @param file 要压缩的文件或目录
     * @param zos 输出流
     * @param baseDir 基准目录
     * @throws IOException 压缩出错时抛出
     */
    private static void compress(File file, ZipOutputStream zos, File baseDir) throws IOException {
        String entryName = baseDir.toURI().relativize(file.toURI()).getPath();

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                // 空目录也要创建
                zos.putNextEntry(new ZipEntry(entryName + "/"));
                zos.closeEntry();
            } else {
                for (File child : files) {
                    compress(child, zos, baseDir);
                }
            }
        } else {
            try (FileInputStream fis = new FileInputStream(file)) {
                zos.putNextEntry(new ZipEntry(entryName));
                byte[] buffer = new byte[4096];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
            }
        }
    }
}
