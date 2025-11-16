package com.spring.springboot.UrlShortener.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "informationOfLinksInUrlShortener")
public class LinkInformation {
}
