package io.github.cc53453.common.exception;

import io.github.cc53453.common.enums.ThirdPartySystem;
import lombok.Getter;
import lombok.ToString;

/**
 * 自定义的第三方系统api调用返回报错时的异常
 */
@Getter
@ToString
public class ThirdPartyApiResponseException extends RuntimeException {
    /**
     * 序列化id
     */
    private static final long serialVersionUID = -8041662072987778295L;
    /**
     * 报错码，一般是第三方系统接口返回的http错误码
     */
    private String errorCode;
    /**
     * 哪个第三方系统
     */
    private ThirdPartySystem whichSystem;
    
    /**
     * 构造函数
     * @param message 错误信息
     * @param errorCode 错误码
     * @param whichSystem 哪个系统
     */
    public ThirdPartyApiResponseException(String message, String errorCode, ThirdPartySystem whichSystem) {
        super(message);
        this.errorCode = errorCode;
        this.whichSystem = whichSystem;
    }
    
    /**
     * 构造函数
     * @param message 错误信息
     * @param cause 堆栈信息
     * @param errorCode 错误码
     * @param whichSystem 哪个系统
     */
    public ThirdPartyApiResponseException(String message, Throwable cause, String errorCode, ThirdPartySystem whichSystem) {
        super(message, cause);
        this.errorCode = errorCode;
        this.whichSystem = whichSystem;
    }
}
