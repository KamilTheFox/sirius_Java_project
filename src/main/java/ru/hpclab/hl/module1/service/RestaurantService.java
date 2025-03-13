package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.*;
import ru.hpclab.hl.module1.model.Restaurant;

import java.util.*;

@Service
public class RestaurantService {
    private final List<Restaurant> restaurants = new ArrayList<>();

    public Restaurant addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
        return restaurant;
    }

    public List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurants);
    }

    public Restaurant getRestaurantByName(String name) {
        return restaurants.stream()
                .filter(r -> r.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}