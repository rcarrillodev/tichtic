package com.rcdev.tichtic.stats;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;


@Service
public class StatsService {
    private StatsRepository statsRepository;
    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }
    public List<StatsModel> getAllStats() {
        return StreamSupport.stream(statsRepository.findAll().spliterator(),
                false).toList();
    }
}
