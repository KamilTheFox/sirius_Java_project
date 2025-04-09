package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.*;
import ru.hpclab.hl.module1.model.Dish;
import ru.hpclab.hl.module1.model.Restaurant;
import ru.hpclab.hl.module1.repository.DishRepository;

import java.util.*;

@Service
public class DishService {

    private final DishRepository dishes;

    public DishService(DishRepository dishes) {
        this.dishes = dishes;
    }

    public Dish addDish(Dish dish) {
        dishes.save(dish);
        return dish;
    }

    public List<Dish> getAllDishes() {
        return dishes.findAll();
    }

    public Dish getDishById(UUID id) {
        return dishes.getReferenceById(id);
    }

    public List<Dish> getDishesByRestaurant(Restaurant restaurant) {
        return dishes.findByRestaurant(restaurant);
    }

    public List<Dish> getDishesByIds(List<UUID> dishes)
    {
        return this.dishes.findAllById(dishes);
    }

    public void clearAll() {
        dishes.deleteAll();
    }
}
