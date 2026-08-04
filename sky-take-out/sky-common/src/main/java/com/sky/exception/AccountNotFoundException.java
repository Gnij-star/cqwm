package com.sky.exception;

/**
 * 账号不存在异常
 */
public class AccountNotFoundException extends BaseException {
    private static final Integer DEFAULT_CODE = 500;

    public AccountNotFoundException(String msg) {
        super(DEFAULT_CODE,msg);
    }

}
