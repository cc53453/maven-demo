package io.github.cc53453.file.dto;

import lombok.Data;

/**
 * shell执行结果DTO
 */
@Data
public class ShellExecuteResultDTO {
	/**
	 * 执行退出码是否为0
	 */
	private boolean success;
	/**
	 * 执行退出码
	 */
	private int exitCode;
	/**
	 * 执行结果消息（如异常信息）
	 */
	private String message;
	/**
	 * 执行结果输出（如脚本的标准输出和错误输出）
	 */
	private String output;
}
