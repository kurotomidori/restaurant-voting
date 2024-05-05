package ru.bootjava.restaurantvoting.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.restaurantvoting.model.Restaurant;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Override
    @Cacheable(value = "restaurants")
    List<Restaurant> findAll(Sort sort);

    @Override
    @Modifying
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    Restaurant save(Restaurant restaurant);
}