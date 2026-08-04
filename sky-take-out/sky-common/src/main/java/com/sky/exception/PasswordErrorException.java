package com.sky.exception;

/**
 * 密码错误异常
 */
public class PasswordErrorException extends BaseException {
    private static final Integer DEFAULT_CODE = 1007;

    public PasswordErrorException() {
        super(DEFAULT_CODE, "密码错误");   // 调用父类全参构造
    }

    public PasswordErrorException(String msg) {
        super(DEFAULT_CODE, msg);          // 也可以自定义消息，但固定错误码
    }

    public PasswordErrorException(String msg, Throwable cause) {
        super(DEFAULT_CODE, msg, cause);
    }

}
