package ru.bootjava.restaurantvoting.web.restaurant;

import ru.bootjava.restaurantvoting.model.Restaurant;
import ru.bootjava.restaurantvoting.web.MatcherFactory;

import static ru.bootjava.restaurantvoting.web.restaurant.RestaurantController.REST_URL;

public class RestaurantTestData {

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);

    public static final String REST_URL_SLASH = REST_URL + '/';

    public static final int RESTAURANT_1_ID = 1;
    public static final int RESTAURANT_2_ID = 2;
    public static final int RESTAURANT_3_ID = 3;
    public static final int NOT_FOUND = 100;

    public static final String RESTAURANT_1_NAME = "rest_1";
    public static final String RESTAURANT_2_NAME = "rest_2";
    public static final String RESTAURANT_3_NAME = "rest_3";

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_1_ID, RESTAURANT_1_NAME);
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_2_ID, RESTAURANT_2_NAME);
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT_3_ID, RESTAURANT_3_NAME);

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_1_ID, "Updated Restaurant");
    }
}
