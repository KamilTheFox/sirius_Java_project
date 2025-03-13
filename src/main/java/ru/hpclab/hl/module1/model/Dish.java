package ru.hpclab.hl.module1.model;
import java.util.UUID;

public class Dish {
    private UUID identifier;
    private String name;
    private double price;
    private double weight;
    private Restaurant restaurant;

    public Dish(UUID identifier, String name, double price, double weight, Restaurant restaurant) {
        this.identifier = identifier;
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.restaurant = restaurant;
    }

    public Dish() {
    }

    // Геттеры и сеттеры
    public UUID getIdentifier() { return identifier; }
    public void setIdentifier(UUID identifier) { this.identifier = identifier; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }
}
