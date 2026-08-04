package com.sky.exception;

/**
 * 账号被锁定异常
 */
public class AccountLockedException extends BaseException {

    public AccountLockedException(String msg) {
        super(500,msg);
    }

}
