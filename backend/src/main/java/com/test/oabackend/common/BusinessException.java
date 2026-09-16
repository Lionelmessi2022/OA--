package com.test.oabackend.common;

public class BusinessException extends RuntimeException{
    public BusinessException(String message) {
        super(message);
    }
}
