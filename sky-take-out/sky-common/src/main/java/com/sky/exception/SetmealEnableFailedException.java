package com.sky.exception;

/**
 * 套餐启用失败异常
 */
public class SetmealEnableFailedException extends BaseException {
    private static final Integer DEFAULT_CODE = 500;
    public SetmealEnableFailedException(){
        super(DEFAULT_CODE,"套餐启用失败");
    }
    public SetmealEnableFailedException(String msg){
        super(DEFAULT_CODE,msg);
    }

    public SetmealEnableFailedException(String msg,Throwable cause){
        super(DEFAULT_CODE,msg,cause);
    }
}
