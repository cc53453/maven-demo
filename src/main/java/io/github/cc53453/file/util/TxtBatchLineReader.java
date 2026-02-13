package io.github.cc53453.file.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 游标方式读取txt的reader
 */
@Slf4j
public class TxtBatchLineReader implements Closeable {
    private static final int LINE_BREAK_BYTES_LENGTH = 1;
    private BufferedReader reader;
    // 记录上次next读到的最后一行，本次next需要加上去
    private String lastLine;

    /**
     * 新增实例时会先读取第一行文件内容
     * @param filePath 文件路径
     * @throws IOException 读文件reader.readLine失败时会抛错
     */
    public TxtBatchLineReader(String filePath) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8));
        while ((lastLine = reader.readLine()) != null) {
            if(!lastLine.isBlank()) {
                break;
            }
        }
    }
    
    /**
     * 批量按字数读取文件行（中文/英文都按照byte长度计算）
     * @param charLimit 限制返回的字节数byte
     * @return 符合限制的多行文本，已读到末尾返回 null，如果第一行byte数就超限制了就会抛出异常IllegalArgumentException
     * @throws IOException 读文件reader.readLine失败时会抛错
     */
    public List<String> next(int charLimit) throws IOException {
        if(lastLine == null || charLimit <= 0) {
            // 这里故意返回null，防止调用时循环退出条件写的不对导致死循环
            return null; // NOSONAR
        }
        
        int totalChars = 0;
        List<String> result = new ArrayList<>();
        if(!lastLine.isBlank()) {
            totalChars = lastLine.getBytes(StandardCharsets.UTF_8).length + LINE_BREAK_BYTES_LENGTH;
            
            if (totalChars > charLimit) {
                throw new IllegalArgumentException(String.format(
                        "the first line's length is %d, already gt %d, line: %s", 
                        totalChars, charLimit, lastLine));
            }
            result.add(lastLine);
        }

        String line = null;
        while ((line = reader.readLine()) != null) {
            if(!line.isBlank()) {
                // 还要算上换行符的1位
                int len = line.getBytes(StandardCharsets.UTF_8).length + LINE_BREAK_BYTES_LENGTH;
    
                if (totalChars + len > charLimit) {
                    // 当前行超出限制，不读取，留给下次
                    break;
                }
    
                result.add(line);
                totalChars += len;
            }
        }

        // 记录本次读到的最后一行，下次要带上本行
        lastLine = line;
        return result;
    }
    
    @Override
    public void close() throws IOException {
        reader.close();
    }

}
