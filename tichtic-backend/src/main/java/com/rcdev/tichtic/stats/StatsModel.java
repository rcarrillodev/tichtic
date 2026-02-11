package com.rcdev.tichtic.stats;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stats")
public class StatsModel {
    @Id
    private String shortCode;
    private Long hits;
    private LocalDate lastAccessed;
    private String originalUrl;
    private String createdAt;
}
