package ru.hpclab.hl.module1.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DishCreateDTO {
    private String name;
    private double price;
    private double weight;
    private UUID restaurantId;
}
