package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.*;
import ru.hpclab.hl.module1.model.*;
import ru.hpclab.hl.module1.repository.OrderRepository;

import java.time.*;
import java.util.*;

@Service
public class OrderService
{
    private final OrderRepository orders;

    public OrderService(OrderRepository orders) {
        this.orders = orders;
    }

    public Order createOrder(Restaurant restaurant,
                             List<Dish> dishes,
                             String address)
    {
        // Проверка минимальной суммы заказа
        double orderSum = calculateOrderSum(dishes);

        if (orderSum < restaurant.getMinimumOrder()) {
            throw new IllegalArgumentException(
                    "Сумма заказа меньше минимальной! Минимум: " +
                            restaurant.getMinimumOrder()
            );
        }
        List<UUID> dishIDs = dishes.stream().map(Dish::getIdentifier).toList();
        Order order = new Order(restaurant.getIdentifier(), dishIDs, address, orderSum);
        order.setTotalAmount(orderSum);
        orders.save(order);
        return order;
    }

    private double calculateOrderSum(List<Dish> dishes) {
        return dishes.stream()
                .mapToDouble(Dish::getPrice)
                .sum();
    }

    public List<Order> getAllOrders() {
        return orders.findAll();
    }

    public List<Order> getRestaurantOrders(Restaurant restaurant) {
        return orders.findByRestaurant(restaurant);
    }

    public List<Order> getRestaurantMonthlyOrders(Restaurant restaurant) {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        return orders.findByRestaurantAndDeliveryTimeAfter(restaurant, monthAgo);
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
