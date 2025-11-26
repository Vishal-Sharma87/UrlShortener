package com.spring.springboot.UrlShortener.exceptions;

public class SendgridEmailFailedException extends RuntimeException {
    public SendgridEmailFailedException(String msg) {
        super(msg);
    }
}
