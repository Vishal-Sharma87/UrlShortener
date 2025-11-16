package com.spring.springboot.UrlShortener.responseDtos;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkApiResponseDto<T> {
    private T data;
    private String message;
    private Date timestamp;

}
