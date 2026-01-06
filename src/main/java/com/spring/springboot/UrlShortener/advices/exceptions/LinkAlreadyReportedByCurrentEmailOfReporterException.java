package com.spring.springboot.UrlShortener.advices.exceptions;

public class LinkAlreadyReportedByCurrentEmailOfReporterException extends RuntimeException {
    public LinkAlreadyReportedByCurrentEmailOfReporterException(String msg) {
        super(msg);
    }
}
