package com.spring.springboot.UrlShortener.advices.exceptions;

public class SendgridEmailFailedException extends RuntimeException {
    public SendgridEmailFailedException(String msg) {
        super(msg);
    }
}
