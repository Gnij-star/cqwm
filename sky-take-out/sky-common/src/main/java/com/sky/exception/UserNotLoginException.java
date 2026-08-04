package com.sky.exception;

public class UserNotLoginException extends BaseException {
    // 固定错误码，不再接受外部传入
    private static final Integer DEFAULT_CODE = 401;
    public UserNotLoginException(Integer code,String msg) {
        super(DEFAULT_CODE,msg);
    }

}
