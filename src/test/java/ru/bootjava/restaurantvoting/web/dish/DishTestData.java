package ru.bootjava.restaurantvoting.web.dish;

import ru.bootjava.restaurantvoting.model.Dish;
import ru.bootjava.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;

public class DishTestData {

    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant");

    public static final int DISH_1_ID = 1;
    public static final int DISH_2_ID = 2;
    public static final int DISH_3_ID = 3;
    public static final int NOT_FOUND = 100;

    public static final Dish dish1 = new Dish(DISH_1_ID, "dish1", 100, LocalDate.now());
    public static final Dish dish2 = new Dish(DISH_2_ID, "dish2", 200, LocalDate.now());
    public static final Dish dish3 = new Dish(DISH_3_ID, "old_dish", 300, LocalDate.of(2024, 1, 1));

    public static Dish getNew() {
        return new Dish(null, "new_dish", 400, LocalDate.now());
    }

    public static Dish getUpdated() {
        return new Dish(DISH_1_ID, "updated_dish", 1000, dish1.getDate().minusDays(1));
    }
}
