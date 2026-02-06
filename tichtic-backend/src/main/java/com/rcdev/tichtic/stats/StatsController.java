package com.rcdev.tichtic.stats;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "/stats")
public class StatsController {

    private final StatsService statsService;
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/")
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
    public ResponseEntity<List<StatsModel>> getStats() {
        List<StatsModel> stats = statsService.getAllStats();
        return ResponseEntity.ok(stats);
    }
}