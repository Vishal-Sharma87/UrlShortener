package com.spring.springboot.UrlShortener.exceptions;

public class ResourceNotExistsException extends RuntimeException {

    public ResourceNotExistsException(String msg) {
        super(msg);
    }
}
