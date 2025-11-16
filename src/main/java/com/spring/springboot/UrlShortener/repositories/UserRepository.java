package com.spring.springboot.UrlShortener.repositories;

import com.spring.springboot.UrlShortener.entity.UrlUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<UrlUser, ObjectId> {
    UrlUser findUserByUserName(String userName);

    void deleteUserByUserName(String userName);
}
