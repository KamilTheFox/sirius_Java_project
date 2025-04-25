package ru.hpclab.hl.module1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.dto.AllAverageCheckDTO;
import ru.hpclab.hl.module1.dto.AverageCheckDTO;
import ru.hpclab.hl.module1.dto.OrderCreateDTO;
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
    public ResponseEntity<Order> createOrder(@RequestBody OrderCreateDTO request) {
        try {
            Order curOrder = orderService.createOrder(
                    restaurantService.getRestaurantByUUID(request.getRestaurant()),
                    dishService.getDishesByIds(request.getDishes())
                    ,request.getDeliveryAddress());
            return ResponseEntity.ok(curOrder);
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
        long start = System.currentTimeMillis();
        try {
            Restaurant restaurant = restaurantService.getRestaurantByUUID(restaurantId);
            return ResponseEntity.ok(orderService.getRestaurantOrders(restaurant));
        }
        finally {
            ObservabilityService.recordTiming("getRestaurantOrders", System.currentTimeMillis() - start);
        }
    }

    @GetMapping("/restaurant/{restaurantId}/average-check")
    public ResponseEntity<AverageCheckDTO> getAverageCheck(@PathVariable UUID restaurantId)
    {
        long start = System.currentTimeMillis();
        Restaurant restaurant = restaurantService.getRestaurantByUUID(restaurantId);
        try {
            return ResponseEntity.ok(orderService.calculateAverageCheck(restaurant.getIdentifier()));
        }
        finally {
            ObservabilityService.recordTiming("Get Average-check", System.currentTimeMillis() - start);
        }
    }

    @GetMapping("/restaurants/average-check")
    public ResponseEntity<AllAverageCheckDTO> getAllAverageCheck()
    {
        long start = System.currentTimeMillis();
        try {
            return ResponseEntity.ok(orderService.getAverageCheckAll());
        }
        finally {
            ObservabilityService.recordTiming("Get Average-check", System.currentTimeMillis() - start);
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAll() {
        orderService.clearAll();
        return ResponseEntity.ok().build();
    }
}