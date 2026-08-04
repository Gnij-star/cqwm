package com.sky.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * 业务异常
 */
@Getter
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer code;
    private final String detail;

    // 1. 全参构造（主构造器）
    public BaseException(Integer code, String detail) {
        super(detail);
        this.code = code;
        this.detail = detail;
    }

    // 2. 全参构造 + 异常链（主构造器）
    public BaseException(Integer code, String detail, Throwable cause) {
        super(detail, cause);
        this.code = code;
        this.detail = detail;
    }

    // 3. 【新增】只传 detail，默认 code = 500
    public BaseException(String detail) {
        this(500, detail);  // 调用上面的全参构造
    }

    // 4. 【新增】传 detail 和 cause，默认 code = 500
    public BaseException(String detail, Throwable cause) {
        this(500, detail, cause);  // 调用上面的全参构造 + cause
    }
}
