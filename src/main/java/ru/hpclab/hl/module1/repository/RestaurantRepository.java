package ru.hpclab.hl.module1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hpclab.hl.module1.model.Restaurant;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID>
{
    Optional<Restaurant> findByName(String name);
    @Query("SELECT r FROM Restaurant r WHERE r.identifier = :id")
    Optional<Restaurant> findByIdentifier(@Param("id") UUID id);
}
