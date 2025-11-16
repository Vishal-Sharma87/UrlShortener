package com.spring.springboot.UrlShortener.exceptions;

public class ResourceWithHashNotExistsException extends RuntimeException {

    public ResourceWithHashNotExistsException(String msg) {
        super(msg);
    }

}
