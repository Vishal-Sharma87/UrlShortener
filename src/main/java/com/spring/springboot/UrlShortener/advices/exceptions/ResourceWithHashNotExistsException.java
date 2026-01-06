package com.spring.springboot.UrlShortener.advices.exceptions;

public class ResourceWithHashNotExistsException extends RuntimeException {

    public ResourceWithHashNotExistsException(String msg) {
        super(msg);
    }

}
