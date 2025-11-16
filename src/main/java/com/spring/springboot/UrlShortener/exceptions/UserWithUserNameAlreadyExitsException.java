package com.spring.springboot.UrlShortener.exceptions;

public class UserWithUserNameAlreadyExitsException extends RuntimeException{
    public UserWithUserNameAlreadyExitsException(String msg){
        super(msg);
    }
}
