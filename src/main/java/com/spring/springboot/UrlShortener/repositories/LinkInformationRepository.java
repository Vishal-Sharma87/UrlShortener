package com.spring.springboot.UrlShortener.repositories;

import com.spring.springboot.UrlShortener.entity.Links;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LinkInformationRepository extends MongoRepository<Links, Long> {
}
