package ru.bootjava.restaurantvoting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.restaurantvoting.model.Meal;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MealRepository extends BaseRepository<Meal> {

    @Query("SELECT m FROM Meal m WHERE m.restaurant.id=:restaurantId ORDER BY m.date DESC, m.name")
    List<Meal> getAll(int restaurantId);

    @Query("SELECT m FROM Meal m WHERE m.restaurant.id=:restaurantId AND m.date=:date ORDER BY m.name")
    List<Meal> getAllOnSpecificDate(int restaurantId, LocalDate date);
}