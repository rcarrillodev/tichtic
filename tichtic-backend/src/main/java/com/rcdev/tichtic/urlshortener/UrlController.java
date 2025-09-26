package com.rcdev.tichtic.urlshortener;

import com.rcdev.tichtic.stats.StatsMessagePublisher;
import com.rcdev.tichtic.stats.dto.StatsMessage;
import com.rcdev.tichtic.urlshortener.dto.UrlDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/")
public class UrlController {

    private UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
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
        String url = urlService.getExpandedUrl(shortCode);
        return new ModelAndView("redirect:" + url);
    }
}
