package ru.hpclab.hl.module1.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.dto.RestaurantCreateDTO;
import ru.hpclab.hl.module1.model.*;
import ru.hpclab.hl.module1.service.*;

import java.util.List;

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
        return ResponseEntity.ok(restaurantService.addRestaurant(
                new Restaurant(
                        restaurant.getName(),
                        restaurant.getCuisine(),
                        restaurant.getMinimumOrder())));
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants()
    {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Restaurant> getRestaurantByName(@PathVariable String name) {
        Restaurant restaurant = restaurantService.getRestaurantByName(name);
        return restaurant != null ? ResponseEntity.ok(restaurant) : ResponseEntity.notFound().build();
    }
}
