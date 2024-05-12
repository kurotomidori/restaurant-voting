package ru.bootjava.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.restaurantvoting.model.Dish;
import ru.bootjava.restaurantvoting.repository.DishRepository;
import ru.bootjava.restaurantvoting.repository.RestaurantRepository;

@Service
@AllArgsConstructor
public class DishService {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    @CacheEvict(value = "currentdishes", key = "#restaurantId")
    public Dish save(int restaurantId, Dish dish) {
        dish.setRestaurant(restaurantRepository.getExisted(restaurantId));
        return dishRepository.save(dish);
    }
}
