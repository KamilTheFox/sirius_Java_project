package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.*;
import ru.hpclab.hl.module1.service.*;
import ru.hpclab.hl.module1.dto.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderCreateRequest request) {
        try {
            Order order = orderService.createOrder(
                    request.getIdentifier(),
                    request.getRestaurant(),
                    request.getDishes(),
                    request.getAddress(),
                    request.getOrderTime()
            );
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getRestaurantOrders(@PathVariable UUID restaurantId) {
        Restaurant restaurant = new Restaurant(); // Здесь нужно получить ресторан
        return ResponseEntity.ok(orderService.getRestaurantOrders(restaurant));
    }

    @GetMapping("/restaurant/{restaurantId}/average-check")
    public ResponseEntity<Double> getAverageCheck(@PathVariable UUID restaurantId) {
        Restaurant restaurant = new Restaurant(); // Здесь нужно получить ресторан
        return ResponseEntity.ok(orderService.calculateAverageCheck(restaurant));
    }
}