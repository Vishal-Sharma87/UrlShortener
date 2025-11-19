package com.spring.springboot.UrlShortener.exceptions;

public class LinkAlreadyReportedByCurrentEmailOfReporterException extends RuntimeException {
    public LinkAlreadyReportedByCurrentEmailOfReporterException(String msg) {
        super(msg);
    }
}
