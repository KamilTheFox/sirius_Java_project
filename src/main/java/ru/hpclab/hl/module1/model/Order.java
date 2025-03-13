package ru.hpclab.hl.module1.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {
    private UUID identifier;
    private Restaurant restaurant;
    private List<Dish> dishes;
    private String deliveryAddress;
    private LocalDateTime deliveryTime;
    private double totalAmount;

    public Order(UUID identifier, Restaurant restaurant, List<Dish> dishes,
                 String deliveryAddress, LocalDateTime deliveryTime) {
        this.identifier = identifier;
        this.restaurant = restaurant;
        this.dishes = dishes;
        this.deliveryAddress = deliveryAddress;
        this.deliveryTime = deliveryTime;
        this.calculateTotalAmount();
    }

    public Order() {
    }

    private void calculateTotalAmount() {
        this.totalAmount = dishes.stream()
                .mapToDouble(Dish::getPrice)
                .sum();
    }

    // Геттеры и сеттеры
    public UUID getIdentifier() { return identifier; }
    public void setIdentifier(UUID identifier)
    {
        this.identifier = identifier;
    }

    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant)
    {
        this.restaurant = restaurant;
    }

    public List<Dish> getDishes() { return dishes; }
    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
        this.calculateTotalAmount();
    }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress)
    {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getDeliveryTime()
    {
        return deliveryTime;
    }
    public void setDeliveryTime(LocalDateTime deliveryTime)
    {
        this.deliveryTime = deliveryTime;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }
    public void setTotalAmount(double amount)
    {
        totalAmount = amount;
    }
}