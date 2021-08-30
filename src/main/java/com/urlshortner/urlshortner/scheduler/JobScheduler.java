package com.urlshortner.urlshortner.scheduler;

import com.urlshortner.urlshortner.model.Url;
import com.urlshortner.urlshortner.repository.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class JobScheduler {

    @Autowired
    private UrlRepository urlRepository;

    @Scheduled(cron = "0 0 0 * * ?") // every night 12AM
    public void deactivateUrl(){
        List<Url> urls = urlRepository.findAll();
        for(Url url : urls){
            if(url.getExpirationDate().isBefore(LocalDateTime.now())){
                log.info("Url deleted with Id :" + url.getShortLink());
                urlRepository.delete(url);
            }
        }

    }
}
