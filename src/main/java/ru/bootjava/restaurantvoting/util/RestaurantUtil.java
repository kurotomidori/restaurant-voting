package ru.bootjava.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import ru.bootjava.restaurantvoting.model.Restaurant;
import ru.bootjava.restaurantvoting.model.Vote;
import ru.bootjava.restaurantvoting.to.RestaurantTo;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;

@UtilityClass
public class RestaurantUtil {

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants, Vote vote) {
        if (vote != null) {
            return createList(restaurants, vote, (r, v) -> r.id() == v.getRestaurantId());
        }
        return createList(restaurants, null, (r, v) -> false);
    }

    public static List<RestaurantTo> createList(Collection<Restaurant> restaurants, Vote vote, BiPredicate<Restaurant, Vote> predicate) {
        return restaurants.stream()
                .map(restaurant -> createTo(restaurant, predicate.test(restaurant, vote)))
                .toList();
    }

    public static RestaurantTo createTo(Restaurant restaurant, boolean isVoted) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), isVoted);
    }
}
