package com.urlshortner.urlshortner.service;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.urlshortner.urlshortner.Request.UrlRequest;
import com.urlshortner.urlshortner.model.Url;
import com.urlshortner.urlshortner.repository.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@Slf4j
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;
    public Url generateShortLink(UrlRequest originalUrl) {
        String origUrl = originalUrl.getUrl();
        UrlValidator validator = new UrlValidator(
                new String[]{"http", "https"}
        );
        if(validator.isValid(origUrl)){
            String id = Hashing.murmur3_32()
                        .hashString(origUrl.concat(LocalDateTime.now().toString()),StandardCharsets.UTF_8)
                        .toString();
            log.info("Url Id generate : "+ id);

            Url url = new Url();
            url.setCreationDate(LocalDateTime.now());
            url.setOriginalUrl(origUrl);
            url.setShortLink(id);
            url.setExpirationDate(getExpirationTime(originalUrl.getExpirationDate(), url.getCreationDate()));
            return urlRepository.save(url);
        }else
            throw new RuntimeException("Invalid Url...!");
    }

    private LocalDateTime getExpirationTime(String expirationDate,LocalDateTime creationDate) {
        if(Strings.isNullOrEmpty(expirationDate)){
            return creationDate.plusSeconds(24*60*60); //1day
        }
        return LocalDateTime.parse(expirationDate);

    }

    public String redirectToOriginalUrl(String id) {
        Url url = urlRepository.findByShortLink(id);
        if(url==null)
            return null;
        if(url.getExpirationDate().isBefore(LocalDateTime.now())){
            urlRepository.delete(url);
            return "expired";
        }
        return url.getOriginalUrl();
    }
}
