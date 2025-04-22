package ru.hpclab.hl.module1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hpclab.hl.module1.model.Order;
import ru.hpclab.hl.module1.model.Restaurant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    // Поиск по ресторану
    List<Order> findByRestaurant(UUID restaurant);

    // Поиск заказов ресторана за последний месяц
    @Query("SELECT o FROM Order o WHERE o.restaurant = :restaurant " +
            "AND o.deliveryTime > :monthAgo")
    List<Order> findByRestaurantAndDeliveryTimeAfter(
            @Param("restaurant") UUID restaurant,
            @Param("monthAgo") LocalDateTime monthAgo
    );

    // Для подсчета среднего чека
    @Query("SELECT AVG(o.totalAmount) FROM Order o " +
            "WHERE o.restaurant = :restaurant " +
            "AND o.deliveryTime > :monthAgo")
    Double calculateAverageCheckForPeriod(
            @Param("restaurant") UUID restaurant,
            @Param("monthAgo") LocalDateTime monthAgo
    );

    @Modifying
    @Query(value = "DELETE FROM orders", nativeQuery = true)
    void deleteAllOrdersNative();

}

