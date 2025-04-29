package ru.hpclab.hl.module1.dto;

import lombok.Data;

@Data
public class KafkaMessage {
    private String entity;       // RESTAURANT, ORDER, DISH
    private String operation;    // GET, POST
    private String payload;      // JSON строка с данными
}
