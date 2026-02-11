package com.rcdev.tichtic.urlshortener;

import com.google.common.hash.Hashing;
import com.rcdev.tichtic.urlshortener.dto.UrlDTO;
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

@Service
public class UrlService {
    @Value("${shortener.defaultExpireInDays:7}")
    private int defaultExpireInDays;
    @Value("${shortener.urlPrefix: ''}")
    private String urlPrefix;
    @Value("${shortener.maxShortUrlLength: 8}")
    private Integer maxShortUrlLength;

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

        String shortcode = hashUrl(originalUrl);
        UrlModel newUrl;

        try {
            return getExpandedUrl(shortcode);
        } catch (ResponseStatusException e){
            logger.info("Creating new short url");
            newUrl = new UrlModel();
            newUrl.setOriginalUrl(originalUrl);
        }
        newUrl.setShortCode(shortcode);
        newUrl.setCreatedAt(LocalDateTime.now());
        newUrl.setExpiresOn(LocalDateTime.now().plusDays(defaultExpireInDays));
        urlRepository.save(newUrl);
        return toUrlDto(newUrl);
    }

    @Cacheable(value = "shortUrl", key = "#shortCode")
    public UrlDTO getExpandedUrl(String shortCode){
        UrlModel urlModel = urlRepository.getByShortCode(shortCode);
        if (Objects.isNull(urlModel)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shortcode not found");
        }
        return toUrlDto(urlModel);
    }

    @Scheduled(fixedRate = 100000)
    public void removeExpired(){
        logger.info("Running scheduled task to remove expired records");
        List<UrlModel> toDelete = urlRepository.findAllWithExpiresOnAfterToday(LocalDateTime.now());
        logger.info("Deleting {} records", toDelete.size());
        toDelete.forEach(urlRepository::delete);
    }

    private String hashUrl(String url) {
        byte[] hashedUrlBytes = Hashing.murmur3_128().hashUnencodedChars(url).asBytes();
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hashedUrlBytes){
            stringBuilder.append(String.format("%02x",b));
        }
        return stringBuilder.substring(0, Math.min(maxShortUrlLength, stringBuilder.length()));
    }

    private UrlDTO toUrlDto(UrlModel urlModel){
        UrlDTO result = new UrlDTO();
        result.setOriginalUrl(urlModel.getOriginalUrl());
        result.setShortenedUrl(String.format("%s/%s",urlPrefix,urlModel.getShortCode()));
        result.setCreatedAt(urlModel.getCreatedAt().toString());
        result.setShortCode(urlModel.getShortCode());
        return result;
    }
}
