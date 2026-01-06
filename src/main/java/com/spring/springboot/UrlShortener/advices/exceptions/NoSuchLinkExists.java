package com.spring.springboot.UrlShortener.advices.exceptions;

public class NoSuchLinkExists extends RuntimeException {

    public NoSuchLinkExists(String msg) {
        super(msg);
    }
}
