package com.rcdev.tichtic.urlshortener;

import com.rcdev.tichtic.urlshortener.dto.UrlDTO;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

@Service
public class UrlService {
    @Value("${shortener.defaultExpireInDays:7}")
    private int defaultExpireInDays;
    @Value("${shortener.urlPrefix: ''}")
    private String urlPrefix;
    private UrlRepository urlRepository;

    private static final Logger logger = LoggerFactory.getLogger(UrlService.class);

    public UrlService (UrlRepository urlRepository){
        this.urlRepository = urlRepository;
    }

    public UrlDTO createUrl(String originalUrl){
        try {
            new URI(originalUrl).toURL();
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid URL", e);
        }
        UrlModel newUrl = new UrlModel();
        newUrl.setOriginalUrl(originalUrl);
        RandomStringGenerator randomStringGenerator = RandomStringGenerator.builder().withinRange('0','z').filteredBy(LETTERS,DIGITS).get();
        String shortcode = randomStringGenerator.generate(10);
        while (urlRepository.existsByShortCode(shortcode)){
            shortcode = randomStringGenerator.generate(10);
        }
        newUrl.setShortCode(shortcode);
        newUrl.setCreatedAt(LocalDateTime.now());
        newUrl.setExpiresOn(LocalDateTime.now().plusSeconds(10));
        urlRepository.save(newUrl);
        UrlDTO result = new UrlDTO();
        result.setUrl(originalUrl);
        result.setShortenedUrl(String.format("%s/%s",urlPrefix,shortcode));
        return result;
    }

    @Cacheable(value = "shortUrl", key = "#shortCode")
    public String getExpandedUrl(String shortCode){
        UrlModel urlModel = urlRepository.getByShortCode(shortCode);
        if (Objects.isNull(urlModel)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shortcode not found");
        }
        return urlModel.getOriginalUrl();
    }

    @Scheduled(fixedRate = 100000)
    public void removeExpired(){
        logger.info("Running scheduled task to remove expired records");
        List<UrlModel> toDelete = urlRepository.findAllWithExpiresOnAfterToday(LocalDateTime.now());
        logger.info("Deleting {} records", toDelete.size());
        toDelete.forEach(urlRepository::delete);
    }
}
