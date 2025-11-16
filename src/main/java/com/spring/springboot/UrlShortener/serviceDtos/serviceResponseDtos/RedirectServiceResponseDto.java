package com.spring.springboot.UrlShortener.serviceDtos.serviceResponseDtos;


import com.spring.springboot.UrlShortener.utils.virusTotalUtils.virusTotalServices.FinalVerdict;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RedirectServiceResponseDto {
    private FinalVerdict.Verdict status;
    private String actualUrl;
}
