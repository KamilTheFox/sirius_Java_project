package ru.hpclab.hl.module1.dto;

import jakarta.persistence.Column;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RestaurantCreateDTO
{
    private String name;
    private String cuisine;
    private double minimumOrder;
}
