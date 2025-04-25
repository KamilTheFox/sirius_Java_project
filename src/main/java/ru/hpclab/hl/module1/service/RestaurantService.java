package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.*;
import ru.hpclab.hl.module1.model.Restaurant;
import ru.hpclab.hl.module1.repository.RestaurantRepository;

import java.util.*;

@Service
public class RestaurantService
{
    private final RestaurantRepository restaurants;

    public RestaurantService(RestaurantRepository restaurants) {
        this.restaurants = restaurants;
    }

    public Restaurant addRestaurant(Restaurant restaurant) {
        restaurants.save(restaurant);
        return restaurant;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurants.findAll();
    }

    public Restaurant getRestaurantByName(String name) {
        return restaurants.findByName(name).orElse(null);
    }
    public Restaurant getRestaurantByUUID(UUID id)
    {
        long start = System.currentTimeMillis();
        try {
            return restaurants.findByIdentifier(id).orElse(null);
        }
        finally {
            ObservabilityService.recordTiming("Get SQL RestaurantByUUID", System.currentTimeMillis() - start);
        }
    }
    public void clearAll() {
        restaurants.deleteAll();
    }
}