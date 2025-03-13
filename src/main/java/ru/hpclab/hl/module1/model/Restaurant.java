package ru.hpclab.hl.module1.model;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "restaurant")
public class Restaurant {
    @Id
    private UUID identifier;
    @Column
    private String name;
    @Column
    private String cuisine;
    @Column
    private double minimumOrder;

    public Restaurant(String name, String cuisine, double minimumOrder) {
        this.identifier = UUID.randomUUID();
        this.name = name;
        this.cuisine = cuisine;
        this.minimumOrder = minimumOrder;
    }

    public Restaurant() {
    }

}