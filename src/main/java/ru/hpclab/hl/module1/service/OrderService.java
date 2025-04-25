package ru.hpclab.hl.module1.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.hpclab.hl.module1.dto.AllAverageCheckDTO;
import ru.hpclab.hl.module1.dto.AverageCheckDTO;
import ru.hpclab.hl.module1.model.*;
import ru.hpclab.hl.module1.repository.OrderRepository;

import java.time.*;
import java.util.*;

@Service
public class OrderService
{
    private final OrderRepository orders;

    private final RestTemplate restTemplate;

    private final String AVERAGE_CHECK_SERVICE_URL = "http://additional:8081/average-check";

    public OrderService(OrderRepository orders, RestTemplate restTemplate) {
        this.orders = orders;
        this.restTemplate = restTemplate;
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

    public List<Order> getRestaurantMonthlyOrders(Restaurant restaurant) {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        return orders.findByRestaurantAndDeliveryTimeAfter(restaurant.getIdentifier(), monthAgo);
    }

    public AllAverageCheckDTO getAverageCheckAll()
    {
        String url = AVERAGE_CHECK_SERVICE_URL + "/restaurants";
        ResponseEntity<AllAverageCheckDTO> restaurantResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AllAverageCheckDTO>() {});
        return restaurantResponse.getBody();
    }



    @Transactional
    public void clearAll() {
        orders.deleteAllOrdersNative();
    }
}
