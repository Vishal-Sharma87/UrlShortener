package com.spring.springboot.UrlShortener.advices;

import com.spring.springboot.UrlShortener.exceptions.AllFieldsNullException;
import com.spring.springboot.UrlShortener.exceptions.ResourceNotExistsException;
import com.spring.springboot.UrlShortener.exceptions.ResourceWithHashNotExistsException;
import com.spring.springboot.UrlShortener.exceptions.UserWithUserNameAlreadyExitsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AllFieldsNullException.class)
    public ResponseEntity<ApiError> handleAllFieldsNull(AllFieldsNullException ex) {
        ApiError error = new ApiError(ex.getMessage(), 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotExistsException.class)
    public ResponseEntity<ApiError> handleResourceNotExists(ResourceNotExistsException ex) {
        ApiError error = new ApiError(ex.getMessage(), 404);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceWithHashNotExistsException.class)
    public ResponseEntity<ApiError> handleHashNotExists(ResourceWithHashNotExistsException ex) {
        ApiError error = new ApiError(ex.getMessage(), 404);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserWithUserNameAlreadyExitsException.class)
    public ResponseEntity<ApiError> handleUserNameAlreadyExists(UserWithUserNameAlreadyExitsException ex) {
        ApiError error = new ApiError(ex.getMessage(), 409);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // Generic fallback for other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex) {
        ApiError error = new ApiError(ex.getLocalizedMessage(), 500);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAllExceptions(AuthenticationException ex) {
        ApiError error = new ApiError(ex.getLocalizedMessage(), 500);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}