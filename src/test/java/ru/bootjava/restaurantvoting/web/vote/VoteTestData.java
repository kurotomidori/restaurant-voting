package ru.bootjava.restaurantvoting.web.vote;

import ru.bootjava.restaurantvoting.model.Vote;
import ru.bootjava.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;

import static ru.bootjava.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static ru.bootjava.restaurantvoting.web.user.UserTestData.ADMIN_ID;
import static ru.bootjava.restaurantvoting.web.user.UserTestData.USER_ID;

public class VoteTestData {

    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant");

    public static final int VOTE_1_ID = 1;
    public static final int VOTE_2_ID = 2;
    public static final int VOTE_3_ID = 3;
    public static final int VOTE_4_ID = 4;
    public static final int VOTE_NOT_FOUND = 100;

    public static final Vote vote1 = new Vote(VOTE_1_ID, LocalDate.of(2024, 1, 1), RESTAURANT_2_ID, USER_ID);
    public static final Vote vote2 = new Vote(VOTE_2_ID, LocalDate.now(), RESTAURANT_1_ID, USER_ID);
    public static final Vote vote3 = new Vote(VOTE_3_ID, LocalDate.of(2024, 1, 1), RESTAURANT_3_ID, ADMIN_ID);
    public static final Vote vote4 = new Vote(VOTE_4_ID, LocalDate.now(), RESTAURANT_2_ID, ADMIN_ID);

    public static Vote getNew() {
        return new Vote(null, LocalDate.of(2042, 1, 1), RESTAURANT_3_ID, USER_ID);
    }

    public static Vote getChanged() {
        return new Vote(VOTE_2_ID, vote2.getDate(), RESTAURANT_3_ID, USER_ID);
    }
}
