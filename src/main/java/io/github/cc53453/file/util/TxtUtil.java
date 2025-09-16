package io.github.cc53453.file.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.github.cc53453.file.dto.TxtLineDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * txt文件助手
 */
@Slf4j
public class TxtUtil {
    /**
     * 工具类，不支持实例化
     */
    private TxtUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 按行读取txt文件，每行按splitBy分割字符串，然后获得一个List列表，然后调用TxtLineDTO接口的getDataFromList转化为dto对象并返回。
     * @param <T> 必须实现TxtLineDTO接口的getDataFromList，且有无参数的构造函数
     * @param filePath txt文件路径
     * @param startLineNum 从哪一行开始读，从0开始
     * @param endLineNum 读到哪一行结束，可以用负数，-1表示读到最后一行。
     * @param splitBy 以哪个字符分割每一行字符串
     * @param clazz 需要转化的dto对象的class类
     * @return dto对象的List
     * @throws SecurityException new实例失败时抛错
     * @throws NoSuchMethodException new实例失败时抛错
     * @throws InvocationTargetException new实例失败时抛错
     * @throws IllegalArgumentException 起始结束行号不合理或new实例失败时抛错
     * @throws IllegalAccessException new实例失败时抛错
     * @throws InstantiationException new实例失败时抛错
     */
    public static <T extends TxtLineDTO> List<T> readTxt(String filePath, long startLineNum, 
            long endLineNum, String splitBy, Class<T> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        log.info("readTxt start, filePath: {}, startLineNum: {}, endLineNum: {}, splitBy: {}", 
                filePath, startLineNum, endLineNum, splitBy);
        
        Long maxLineNum = FileBaseUtil.fileLineCount(filePath);
        if(maxLineNum == null) {
            log.error("readTxt failed because of fileLineCount failed, maybe not find the file");
            return Collections.emptyList();
        }
        
        if(endLineNum < 0) {
            endLineNum = maxLineNum + endLineNum;
        }
        
        if(startLineNum>maxLineNum-1 || endLineNum>maxLineNum-1 || startLineNum>endLineNum) {
            log.error("invalid startLineNum: {} or endLineNum: {}, maxLine: {}", startLineNum, endLineNum, maxLineNum);
            throw new IllegalArgumentException("invalid startLineNum or endLineNum");
        }
        
        List<T> result = new ArrayList<>();

        int lineCnt=0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while(lineCnt < startLineNum && 
                    (line = br.readLine()) != null) {
                lineCnt++;
            }

            while ((line = br.readLine()) != null && 
                    lineCnt <= endLineNum) {
                lineCnt++;
                T instance = clazz.getConstructor().newInstance();
                instance.getDataFromList(Arrays.asList(line.split(splitBy)));
                result.add(instance);
            }
        } catch (IOException e) {
            log.error("readfile failed: {}", e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 把内容写到txt文件中
     * @param filePath 文件路径
     * @param contents 内容，list的一个元素对应一行
     * @return 是否写成功
     */
    public static boolean write(String filePath, List<String> contents) {
        // 目标文件路径
        Path path = Paths.get(filePath);

        try {
            StringBuilder sb = new StringBuilder();
            for(String content:contents) {
                sb.append(content).append("\n");
            }
            Files.write(path, sb.toString().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            log.error("Failed to create or write to file.", e);
            return false;
        }
    }
}
