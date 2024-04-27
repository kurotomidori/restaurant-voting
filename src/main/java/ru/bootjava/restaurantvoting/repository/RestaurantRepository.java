package ru.bootjava.restaurantvoting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.restaurantvoting.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}