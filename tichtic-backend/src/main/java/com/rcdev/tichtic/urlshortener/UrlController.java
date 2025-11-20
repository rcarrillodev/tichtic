package com.rcdev.tichtic.urlshortener;

import java.util.Objects;
import com.rcdev.tichtic.stats.StatsMessagePublisher;
import com.rcdev.tichtic.stats.dto.StatsMessage;
import com.rcdev.tichtic.urlshortener.dto.UrlDTO;
import com.rcdev.tichtic.stats.StatsMessagePublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/")
public class UrlController {

    private UrlService urlService;
    private StatsMessagePublisher statsMessagePublisher;

    public UrlController(UrlService urlService, StatsMessagePublisher statsMessagePublisher){
        this.urlService = urlService;
        this.statsMessagePublisher = statsMessagePublisher;
    }

    @PostMapping
    @CrossOrigin(
    origins = {
        "null",
        "file://",
        "http://localhost",
        "http://localhost:8081",
        "http://apache",
        "http://apache:8081"
    },
    allowedHeaders = "*"
    )
    public ResponseEntity<UrlDTO> createShortUrl(@RequestParam String url){
        return new ResponseEntity<>(urlService.createUrl(url), HttpStatus.OK);
    }

    @GetMapping("/{shortCode}")
    public ModelAndView expandUrl(@PathVariable String shortCode){
        String url = urlService.getExpandedUrl(shortCode).getOriginalUrl();
        if (Objects.nonNull(url)){
            publishVisitStatsMessage(shortCode);
            return new ModelAndView("redirect:" + url);
        } else {
            return new ModelAndView("error/404");
        }
    }

    private void publishVisitStatsMessage(String shortCode){
        StatsMessage statsMessage = new StatsMessage(shortCode);
        statsMessagePublisher.publishStatsMessage(statsMessage);
    }
}
