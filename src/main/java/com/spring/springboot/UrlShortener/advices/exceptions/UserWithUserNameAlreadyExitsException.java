package com.spring.springboot.UrlShortener.advices.exceptions;

public class UserWithUserNameAlreadyExitsException extends RuntimeException{
    public UserWithUserNameAlreadyExitsException(String msg){
        super(msg);
    }
}
