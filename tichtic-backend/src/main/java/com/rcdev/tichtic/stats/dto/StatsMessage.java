package com.rcdev.tichtic.stats.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StatsMessage {
    private final Integer COUNT = 1;
    private final String shortCode;
    private final String originalUrl;
    private final String createdAt;
}
