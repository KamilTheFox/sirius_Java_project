package ru.hpclab.hl.module1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.hpclab.hl.module1.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {
    @NotNull(message = "Ресторан должен быть указан")
    private UUID identifier;

    @NotNull(message = "Ресторан должен быть указан")
    private Restaurant restaurant;

    @NotNull(message = "Список блюд не может быть пустым")
    @Size(min = 1, message = "Заказ должен содержать хотя бы одно блюдо")
    private List<Dish> dishes;

    @NotBlank(message = "Адрес доставки должен быть указан")
    private String address;

    @NotNull(message = "Время заказа должно быть указано")
    private LocalDateTime orderTime;

    // Можно добавить дополнительные поля
    private String comment;
    private String phoneNumber;

    // Методы для удобства работы с объектом
    public double calculateTotalAmount() {
        if (dishes == null || dishes.isEmpty()) {
            return 0.0;
        }
        return dishes.stream()
                .mapToDouble(Dish::getPrice)
                .sum();
    }

    public boolean isValidOrder() {
        return restaurant != null
                && dishes != null
                && !dishes.isEmpty()
                && address != null
                && !address.trim().isEmpty()
                && orderTime != null;
    }
}