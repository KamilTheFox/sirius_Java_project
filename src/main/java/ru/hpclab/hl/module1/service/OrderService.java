package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.*;
import ru.hpclab.hl.module1.model.*;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final List<Order> orders = new ArrayList<>();

    public Order createOrder(UUID identifier,Restaurant restaurant, List<Dish> dishes,
                             String address, LocalDateTime time) {
        // Проверка минимальной суммы заказа
        double orderSum = calculateOrderSum(dishes);
        if (orderSum < restaurant.getMinimumOrder()) {
            throw new IllegalArgumentException(
                    "Сумма заказа меньше минимальной! Минимум: " +
                            restaurant.getMinimumOrder()
            );
        }

        Order order = new Order(identifier,restaurant, dishes, address, time);
        order.setTotalAmount(orderSum);
        orders.add(order);
        return order;
    }

    private double calculateOrderSum(List<Dish> dishes) {
        return dishes.stream()
                .mapToDouble(Dish::getPrice)
                .sum();
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public List<Order> getRestaurantOrders(Restaurant restaurant) {
        return orders.stream()
                .filter(o -> o.getRestaurant().equals(restaurant))
                .collect(Collectors.toList());
    }

    public List<Order> getRestaurantMonthlyOrders(Restaurant restaurant) {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        return orders.stream()
                .filter(o -> o.getRestaurant().equals(restaurant))
                .filter(o -> o.getDeliveryTime().isAfter(monthAgo))
                .collect(Collectors.toList());
    }

    public double calculateAverageCheck(Restaurant restaurant) {
        List<Order> monthlyOrders = getRestaurantMonthlyOrders(restaurant);
        if (monthlyOrders.isEmpty()) {
            return 0.0;
        }
        double totalAmount = monthlyOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
        return totalAmount / monthlyOrders.size();
    }
}
