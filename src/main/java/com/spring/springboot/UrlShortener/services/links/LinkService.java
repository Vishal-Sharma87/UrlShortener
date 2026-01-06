package com.spring.springboot.UrlShortener.services.links;

import com.spring.springboot.UrlShortener.dto.RedirectServiceResponseDto;
import com.spring.springboot.UrlShortener.dto.UrlToShortRequestDto;
import com.spring.springboot.UrlShortener.dto.responseDtos.LinkAsResponseDto;
import com.spring.springboot.UrlShortener.entity.Links;
import com.spring.springboot.UrlShortener.model.LinkCreationDto;
import com.spring.springboot.UrlShortener.repositories.LinkRepository;
import com.spring.springboot.UrlShortener.repositories.MongoLinkService;
import com.spring.springboot.UrlShortener.services.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkService {

    private static final String LINK_CREATION_TOPIC = "URL_SHORTENER_link_creation";
    private final LinkHelperService linkHelperService;
    private final LinkRepository linkRepository;
    private final RedisService redisService;
    private final MongoLinkService mongoLinkService;
    private final KafkaTemplate<String, LinkCreationDto> kafkaTemplate;

    private String actualUrlToShortHashConversion(String longUrl, String userName) {
        /* will do following steps
         * 1 -> get counter value from redis
         * 2 -> convert counter to hash
         * 3 -> pass the required link creation info to kafka topic to handle link creation asynchronously
         * 4 -> immediately send the hash, so that users wait time reduces
         * */

//        step 1 get counter
        Long urlCounter = redisService.getUrlCounter();

//        step 2 generate hash using Base62
        String generatedHash = Base62.encode(urlCounter);

//        step 3 pass required info to kafka topic (link_creation)
        LinkCreationDto dtoToCreate = LinkCreationDto.builder()
                .generatedHash(generatedHash)
                .id(urlCounter)
                .longUrl(longUrl)
                .ownerUserName(userName)
                .build();
        kafkaTemplate.send(LINK_CREATION_TOPIC, dtoToCreate);

//        step 4 return the hash
        return generatedHash;
    }

    public List<LinkAsResponseDto> getAllLinksOfAnUser(String userName, String type) {
        List<Links> allLinksOfAnUserByType = mongoLinkService.getAllLinksOfAnUserByType(userName, type);
        return allLinksOfAnUserByType
                .stream()
                .map(linkHelperService::convertActualLinkToResponseLink)
                .toList();

    }

    public void deleteLinkByHashedKey(String hash) {
        Long idToDelete = Base62.decode(hash);
        deleteLinkById(idToDelete.toString());
    }

    public void deleteLinkById(String idToDelete) {

        long id = Long.parseLong(idToDelete);

        linkRepository.deleteById(id);

    }

    public void deleteAllLinksOfAnUserByType(String userName, String type) {
        mongoLinkService.deleteAllLinksOfAnUserByType(userName, type);
    }

    public Links findLinkOfUserById(String idToFind, String userName) {
        return mongoLinkService.getLinkOfAnUserById(idToFind, userName);
    }

    public Links findLinkOfUserByHashedKey(String hashedKey, String userName) {
        Long idToFind = Base62.decode(hashedKey);

        return mongoLinkService.getLinkOfAnUserById(idToFind.toString(), userName);
    }

    public String generateShortUrl(UrlToShortRequestDto url, String userName) {

        String generatedHash = actualUrlToShortHashConversion(url.getActualUrl(), userName);

        String companyDomain = "localhost:8080/";
        String endPointForHandlingRedirection = "url.shortener/";
        return companyDomain + endPointForHandlingRedirection + generatedHash;
    }

    public void saveNewLink(Links createdLink) {
        linkRepository.save(createdLink);
    }

    public LinkAsResponseDto findLink(String userName, String by, String value) {
        return switch (by) {
            case "id" -> linkHelperService.convertActualLinkToResponseLink(findLinkOfUserById(value, userName));

            case "hashedKey" ->
                    linkHelperService.convertActualLinkToResponseLink(findLinkOfUserByHashedKey(value, userName));

            default -> throw new IllegalArgumentException("Method not allowed, try something else.");
        };
    }

    public void deleteLink(String by, String value) {
        switch (by) {
            case "id" -> deleteLinkById(value);

            case "hashedKey" -> deleteLinkByHashedKey(value);

            default -> throw new IllegalArgumentException("Method not allowed, try something else.");
        }
    }

    public Links findLinkByHashedKey(String hashedKey) {

        Long idToFind = Base62.decode(hashedKey);
        return linkRepository.findById(idToFind).orElse(null);
    }

    public void save(Links link) {
        linkRepository.save(link);
    }

    public RedirectServiceResponseDto getLinkStatusIfExists(String hash) {
        Links link = linkRepository.findById(Base62.decode(hash)).orElse(null);
        return link == null ? null : RedirectServiceResponseDto.builder()
                .status(link.getStatus())
                .shortHash(hash)
                .longUrl(link.getActualUrl())
                .build();
    }

    public Links getLinkByHash(String hash) {
        return linkRepository.findById(Base62.decode(hash)).orElse(null);
    }

}
