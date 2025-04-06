package ru.hpclab.hl.module1.model;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dish")
@Setter
@Getter
public class Dish {
    @Id
    private UUID identifier;
    @Column
    private String name;
    @Column
    private double price;
    @Column
    private double weight;
    @Column
    private UUID restaurant;

    public Dish(String name, double price, double weight, UUID restaurant) {
        this.identifier = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.restaurant = restaurant;
    }

    public Dish()
    {

    }

}
