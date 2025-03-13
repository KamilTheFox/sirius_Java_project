package ru.hpclab.hl.module1.model;
import java.util.UUID;

public class Restaurant {
    private UUID identifier;
    private String name;
    private String cuisine;
    private double minimumOrder;

    public Restaurant(UUID identifier, String name, String cuisine, double minimumOrder) {
        this.identifier = identifier;
        this.name = name;
        this.cuisine = cuisine;
        this.minimumOrder = minimumOrder;
    }

    public Restaurant() {
    }

    // Геттеры и сеттеры
    public UUID getIdentifier() { return identifier; }
    public void setIdentifier(UUID identifier) { this.identifier = identifier; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public double getMinimumOrder() { return minimumOrder; }
    public void setMinimumOrder(double minimumOrder) { this.minimumOrder = minimumOrder; }
}