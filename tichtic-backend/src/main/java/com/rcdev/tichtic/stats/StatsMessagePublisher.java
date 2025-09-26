package com.rcdev.tichtic.stats;

import com.rcdev.tichtic.stats.dto.StatsMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.rcdev.tichtic.stats.kafka.StatsKafkaConfig.KAFKA_STATS_TOPIC;

@Service
public class StatsMessagePublisher {
    private final KafkaTemplate<String, StatsMessage> statsPublisherTemplate;

    public StatsMessagePublisher(KafkaTemplate<String, StatsMessage> statsPublisherTemplate) {
        this.statsPublisherTemplate = statsPublisherTemplate;
    }

    public void publishStatsMessage(StatsMessage message){
        statsPublisherTemplate.send(KAFKA_STATS_TOPIC, message);
    }
}
