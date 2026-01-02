package com.spring.springboot.UrlShortener.services;

import com.spring.springboot.UrlShortener.entity.LinkInformation;
import com.spring.springboot.UrlShortener.repositories.LinkInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkInformationService {

    private final LinkInformationRepository linkInformationRepository;

    public void save(LinkInformation linkInformation) {
        linkInformationRepository.save(linkInformation);
    }

}
