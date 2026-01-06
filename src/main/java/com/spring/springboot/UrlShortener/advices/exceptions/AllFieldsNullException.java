package com.spring.springboot.UrlShortener.advices.exceptions;

public class AllFieldsNullException extends RuntimeException {
    public AllFieldsNullException(String msg) {
        super(msg);
    }
}
