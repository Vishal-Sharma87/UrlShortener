package com.spring.springboot.UrlShortener.dto;


import com.spring.springboot.UrlShortener.thirdPartyUtils.virusTotalUtils.virusTotalServices.FinalVerdict;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RedirectServiceResponseDto {
    private FinalVerdict.Verdict status;
    private String longUrl;
    private String shortHash;
}
