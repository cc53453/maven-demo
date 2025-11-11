package io.github.cc53453.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

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
    
    private static String calcChecksum(File file, String algorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);

        try (FileInputStream fis = new FileInputStream(file);
             DigestInputStream dis = new DigestInputStream(fis, digest)) {

            byte[] buffer = new byte[8192];
            // 读取文件数据并更新digest
            while (dis.read(buffer) != -1) {
                // 自动更新digest
            }
        }

        byte[] hash = digest.digest();
        // Java 17+ 可用 HexFormat
        return HexFormat.of().formatHex(hash);
    }

    public static String sha1(File file) throws IOException, NoSuchAlgorithmException {
        return calcChecksum(file, "SHA-1");
    }

    public static String sha256(File file) throws IOException, NoSuchAlgorithmException {
        return calcChecksum(file, "SHA-256");
    }

    public static String md5(File file) throws IOException, NoSuchAlgorithmException {
        return calcChecksum(file, "MD5");
    }
}
