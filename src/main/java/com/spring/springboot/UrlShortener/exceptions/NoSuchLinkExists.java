package com.spring.springboot.UrlShortener.exceptions;

public class NoSuchLinkExists extends RuntimeException {

    public NoSuchLinkExists(String msg) {
        super(msg);
    }
}
