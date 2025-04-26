package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;
import ru.hpclab.hl.module1.dto.AllAverageCheckDTO;
import ru.hpclab.hl.module1.dto.AverageCheckDTO;
import ru.hpclab.hl.module1.model.*;
import ru.hpclab.hl.module1.repository.OrderRepository;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService
{
    private final OrderRepository orders;

    private final RestaurantService restaurantService;

    public OrderService(OrderRepository orders,
                        RestaurantService restaurantService) {
        this.orders = orders;
        this.restaurantService = restaurantService;
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
        return orders.findByRestaurant(restaurant.getIdentifier());
    }

    public AllAverageCheckDTO calculateAverageChecks()
    {
        AllAverageCheckDTO result = new AllAverageCheckDTO();

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        orders.findAll().stream()
                .filter(order -> order.getDeliveryTime().isAfter(oneMonthAgo))
                .collect(Collectors.groupingBy(
                        order ->
                        {
                            UUID id = order.getRestaurant();
                            return restaurantService.getRestaurantByUUID(id).getName();
                        },
                        Collectors.averagingDouble(Order::getTotalAmount)
                ))
                .forEach((name, average) -> {
                    AverageCheckDTO dto = new AverageCheckDTO();
                    dto.setNameRestaurant(name);
                    dto.setAverage_Check(average);
                    result.getAllCheck().add(dto);
                });

        return result;
    }

    public List<Order> getRestaurantMonthlyOrders(Restaurant restaurant) {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        return orders.findByRestaurantAndDeliveryTimeAfter(restaurant.getIdentifier(), monthAgo);
    }

    public AverageCheckDTO calculateAverageCheck(Restaurant restaurant) {
        List<Order> monthlyOrders = getRestaurantMonthlyOrders(restaurant);
        if (monthlyOrders.isEmpty()) {
            return null;
        }
        double totalAmount = monthlyOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        AverageCheckDTO average = new AverageCheckDTO();
        average.setAverage_Check(totalAmount / monthlyOrders.size());
        average.setNameRestaurant(restaurant.getName());

        return average;
    }

    @Transactional
    public void clearAll() {
        orders.deleteAllOrdersNative();
    }
}
