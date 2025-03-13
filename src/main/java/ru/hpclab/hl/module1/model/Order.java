package ru.hpclab.hl.module1.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order
{
    @Id
    private UUID identifier;
    @Column
    private UUID restaurant;
    @Column
    private List<UUID> dishes;
    @Column
    private String deliveryAddress;
    @Column
    private LocalDateTime deliveryTime;
    @Column
    private double totalAmount;

    public Order(UUID restaurant,
                 List<UUID> dishes,
                 String deliveryAddress,
                 double totalAmount)
    {
        this.identifier = UUID.randomUUID();
        this.restaurant = restaurant;
        this.dishes = dishes;
        this.deliveryAddress = deliveryAddress;
        this.deliveryTime = LocalDateTime.now();
        this.totalAmount = totalAmount;
    }

    public Order()
    {
    }

}