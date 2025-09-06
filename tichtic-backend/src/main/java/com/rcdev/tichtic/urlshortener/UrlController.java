package com.rcdev.tichtic.urlshortener;

import com.rcdev.tichtic.stats.StatsMessagePublisher;
import com.rcdev.tichtic.stats.dto.StatsMessage;
import com.rcdev.tichtic.urlshortener.dto.UrlDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@RequestMapping(path = "/")
public class UrlController {

    private UrlService urlService;
    private StatsMessagePublisher statsMessagePublisher;

    public UrlController(UrlService urlService, StatsMessagePublisher statsMessagePublisher) {
        this.urlService = urlService;
        this.statsMessagePublisher = statsMessagePublisher;
    }

    @PostMapping
    @CrossOrigin(origins = {"null", "file://"}, allowedHeaders = "*")
    public ResponseEntity<UrlDTO> createShortUrl(@RequestParam String url){
        return new ResponseEntity<>(urlService.createUrl(url), HttpStatus.OK);
    }

    @GetMapping("/{shortCode}")
    public ModelAndView expandUrl(@PathVariable String shortCode){
        String url = urlService.getExpandedUrl(shortCode);
        StatsMessage statsMessage = new StatsMessage(shortCode);
        statsMessagePublisher.publishStatsMessage(statsMessage);
        return new ModelAndView("redirect:" + url);
    }
}
