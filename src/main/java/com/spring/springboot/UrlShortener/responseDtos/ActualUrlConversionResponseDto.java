package com.spring.springboot.UrlShortener.responseDtos;

import com.spring.springboot.UrlShortener.entity.Links;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualUrlConversionResponseDto {
    private Links link;
}
