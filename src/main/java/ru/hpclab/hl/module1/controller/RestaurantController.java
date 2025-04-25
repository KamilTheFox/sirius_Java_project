package ru.hpclab.hl.module1.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.dto.RestaurantCreateDTO;
import ru.hpclab.hl.module1.model.*;
import ru.hpclab.hl.module1.service.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController
{
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody RestaurantCreateDTO restaurant) {
        long start = System.currentTimeMillis();
        try {
            return ResponseEntity.ok(restaurantService.addRestaurant(
                    new Restaurant(
                            restaurant.getName(),
                            restaurant.getCuisine(),
                            restaurant.getMinimumOrder())));
        }
        finally {
            ObservabilityService.recordTiming("Post Restaurant", System.currentTimeMillis() - start);
        }
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants()
    {
        long start = System.currentTimeMillis();
        try {
            return ResponseEntity.ok(restaurantService.getAllRestaurants());
        }
        finally {
            ObservabilityService.recordTiming("Get Restaurants", System.currentTimeMillis() - start);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantByID(@PathVariable UUID id) {
        long start = System.currentTimeMillis();
        try {
            Restaurant restaurant = restaurantService.getRestaurantByUUID(id);
            return restaurant != null ? ResponseEntity.ok(restaurant) : ResponseEntity.notFound().build();
        }
        finally {
            ObservabilityService.recordTiming("Get Restaurant ByID", System.currentTimeMillis() - start);
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAll() {
        restaurantService.clearAll();
        return ResponseEntity.ok().build();
    }
}
