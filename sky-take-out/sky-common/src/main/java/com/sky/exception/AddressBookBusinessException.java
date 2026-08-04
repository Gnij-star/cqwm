package com.sky.exception;

public class AddressBookBusinessException extends BaseException {

    private static final Integer DEFAULT_CODE = 500;

    public AddressBookBusinessException(Integer code,String msg) {
        super(DEFAULT_CODE,msg);
    }

}
