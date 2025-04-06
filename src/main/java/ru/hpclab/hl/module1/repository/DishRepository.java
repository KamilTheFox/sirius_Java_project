package ru.hpclab.hl.module1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hpclab.hl.module1.model.*;

import java.util.List;
import java.util.UUID;

@Repository
public interface DishRepository extends JpaRepository<Dish, UUID>
{
    List<Dish> findByRestaurant(Restaurant restaurant);
    List<Dish> findAllByIdentifierIn(List<UUID> identifiers);
}