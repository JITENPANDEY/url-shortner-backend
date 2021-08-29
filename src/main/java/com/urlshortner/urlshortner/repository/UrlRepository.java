package com.urlshortner.urlshortner.repository;

import com.urlshortner.urlshortner.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends MongoRepository<Url, String> {
    Url findByShortLink(String id);
}
