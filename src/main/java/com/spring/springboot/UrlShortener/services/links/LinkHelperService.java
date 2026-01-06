package com.spring.springboot.UrlShortener.services.links;

import com.spring.springboot.UrlShortener.dto.responseDtos.LinkAsResponseDto;
import com.spring.springboot.UrlShortener.entity.Links;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkHelperService {
    public LinkAsResponseDto convertActualLinkToResponseLink(Links link) {
// ye method or class banayi taki
// actual link entity jo pahle return kar raha tha
// use linkAsResponse entity me convert kar sakun,
// sirf vo field dikhaun jo zaroori hai saari nhi.
        return LinkAsResponseDto.builder()
                .id(link.getId().toString())
                .actualUrl(link.getActualUrl())
                .status(link.getStatus())
                .hashedKey(link.getHashedKey())
                .clickCnt(link.getClickCount())
                .reportCnt(link.getReportCount())
                .creationTime(link.getLinkCreationTime().toInstant())
                .build();
    }
}
