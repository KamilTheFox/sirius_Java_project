package ru.hpclab.hl.module1.service;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

// Health Check
@Component
public class CustomHealthIndicator implements HealthIndicator {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final DataSource dataSource;

    public CustomHealthIndicator(KafkaTemplate<String, String> kafkaTemplate, DataSource dataSource) {
        this.kafkaTemplate = kafkaTemplate;
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        boolean kafkaHealth = checkKafka();
        boolean dbHealth = checkDatabase();

        if (kafkaHealth && dbHealth) {
            return Health.up().build();
        }
        return Health.down()
                .withDetail("kafka", kafkaHealth)
                .withDetail("database", dbHealth)
                .build();
    }

    private boolean checkKafka() {
        try {
            kafkaTemplate.send("test-topic", "health-check").get(1, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkDatabase() {
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(1000);
        } catch (Exception e) {
            return false;
        }
    }
}
