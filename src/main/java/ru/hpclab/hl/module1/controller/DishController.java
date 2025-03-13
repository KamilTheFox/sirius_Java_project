package ru.hpclab.hl.module1.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.*;
import ru.hpclab.hl.module1.service.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    public ResponseEntity<Dish> addDish(@RequestBody Dish dish) {
        return ResponseEntity.ok(dishService.addDish(dish));
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
        Restaurant restaurant = new Restaurant(); // Здесь нужно получить ресторан
        return ResponseEntity.ok(dishService.getDishesByRestaurant(restaurant));
    }
}
