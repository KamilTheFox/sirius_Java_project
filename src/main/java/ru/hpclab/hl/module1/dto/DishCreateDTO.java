package ru.hpclab.hl.module1.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DishCreateDTO {
    private String name;
    private double price;
    private double weight;
    private UUID restaurantId;

    public DishCreateDTO(String name, double price, double weight, UUID restaurantId)
    {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.restaurantId = restaurantId;
    }
}
