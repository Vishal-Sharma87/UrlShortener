package com.spring.springboot.UrlShortener.advices.exceptions;

public class ResourceNotExistsException extends RuntimeException {

    public ResourceNotExistsException(String msg) {
        super(msg);
    }
}
