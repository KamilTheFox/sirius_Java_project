package ru.hpclab.hl.module1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.*;
import ru.hpclab.hl.module1.service.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final RestaurantService restaurantService;
    private final DishService dishService;


    public OrderController(OrderService orderService, RestaurantService restaurantService, DishService dishService) {
        this.orderService = orderService;
        this.restaurantService = restaurantService;
        this.dishService = dishService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order request) {
        try {
            return ResponseEntity.ok(request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders()
    {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getRestaurantOrders(@PathVariable UUID restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantByUUID(restaurantId);
        return ResponseEntity.ok(orderService.getRestaurantOrders(restaurant));
    }

    public List<Dish> getDishesByIds(List<UUID> dishIds)
    {
        return dishIds.stream()
                .map(dishService::getDishById)
                .collect(Collectors.toList());
    }

    @GetMapping("/restaurant/{restaurantId}/average-check")
    public ResponseEntity<Double> getAverageCheck(@PathVariable UUID restaurantId)
    {
        Restaurant restaurant = restaurantService.getRestaurantByUUID(restaurantId);
        return ResponseEntity.ok(orderService.calculateAverageCheck(restaurant));
    }
}