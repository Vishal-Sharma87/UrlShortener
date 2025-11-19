package com.spring.springboot.UrlShortener.exceptions;

public class InvalidOTPException extends RuntimeException {
    public InvalidOTPException(String msg) {
        super(msg);
    }
}
