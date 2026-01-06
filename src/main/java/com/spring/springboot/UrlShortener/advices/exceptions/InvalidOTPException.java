package com.spring.springboot.UrlShortener.advices.exceptions;

public class InvalidOTPException extends RuntimeException {
    public InvalidOTPException(String msg) {
        super(msg);
    }
}
