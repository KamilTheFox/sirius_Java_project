package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.*;
import ru.hpclab.hl.module1.model.Dish;
import ru.hpclab.hl.module1.model.Restaurant;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DishService {
    private final List<Dish> dishes = new ArrayList<>();

    public Dish addDish(Dish dish) {
        dishes.add(dish);
        return dish;
    }

    public List<Dish> getAllDishes() {
        return new ArrayList<>(dishes);
    }

    public Dish getDishById(UUID id) {
        return dishes.stream()
                .filter(d -> d.getIdentifier().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Dish> getDishesByRestaurant(Restaurant restaurant) {
        return dishes.stream()
                .filter(d -> d.getRestaurant().equals(restaurant))
                .collect(Collectors.toList());
    }
}
