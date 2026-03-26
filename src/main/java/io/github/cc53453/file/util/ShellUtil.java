package io.github.cc53453.file.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.cc53453.file.dto.ShellExecuteResultDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * shell工具类
 */
@Slf4j
public class ShellUtil {
    /**
     * 工具类，不支持实例化
     */
    private ShellUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
	/**
     * 执行shell命令，默认超时时间60s
     * @param command 命令
     * @return 执行结果
     */
    public static ShellExecuteResultDTO execute(List<String> command) {
        return execute(command, 60);
    }
    
    /**
     * 执行shell脚本（带超时）
     * @param command 命令
     * @param timeoutSeconds 超时时间（秒）
     * @return 执行结果
     */
    public static ShellExecuteResultDTO execute(List<String> command, int timeoutSeconds) {
        log.debug("command: {}, timeout: {}s", command, timeoutSeconds);
        ShellExecuteResultDTO result = new ShellExecuteResultDTO();
        
        try {
            // 启动进程
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);  // 合并错误输出到标准输出
            
            Process process = pb.start();
            
            // 读取输出
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            // 等待执行完成
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            
            if (!finished) {
                process.destroyForcibly();
                result.setSuccess(false);
                result.setMessage(String.format("time out of %d(s)!", timeoutSeconds));
                result.setOutput(output.toString());
                return result;
            }
            
            result.setSuccess(process.exitValue() == 0);
            result.setExitCode(process.exitValue());
            result.setMessage(result.isSuccess() ? "success" : "failed");
            result.setOutput(output.toString());
            
        } 
        catch (InterruptedException e) {
        	log.error("do shell failed beacouse InterruptedException: {}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setOutput("");
        	Thread.currentThread().interrupt();
        } 
        catch (IOException e) {
        	log.error("do shell failed because IOException: {}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setOutput("");
		}
        
        return result;
    }
}
