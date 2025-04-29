package ru.hpclab.hl.module1.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class KafkaMessage {
    private String entity;
    private String operation;
    private Object payload;
}
