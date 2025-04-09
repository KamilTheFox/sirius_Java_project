package ru.hpclab.hl.module1.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.dto.DishCreateDTO;
import ru.hpclab.hl.module1.model.*;
import ru.hpclab.hl.module1.service.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishService dishService;
    private final RestaurantService restaurantService;

    public DishController(DishService dishService, RestaurantService restaurantService) {
        this.dishService = dishService;
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<Dish> addDish(@RequestBody DishCreateDTO dish) {
        return ResponseEntity.ok(
                dishService.addDish(
                        new Dish(
                                dish.getName(),
                                dish.getPrice(),
                                dish.getWeight(),
                                dish.getRestaurantId())
                ));
    }

    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable UUID id) {
        Dish dish = dishService.getDishById(id);
        return dish != null ? ResponseEntity.ok(dish) : ResponseEntity.notFound().build();
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Dish>> getDishesByRestaurant(@PathVariable UUID restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantByUUID(restaurantId);
        return ResponseEntity.ok(dishService.getDishesByRestaurant(restaurant));
    }
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAll() {
        dishService.clearAll();
        return ResponseEntity.ok().build();
    }
}
