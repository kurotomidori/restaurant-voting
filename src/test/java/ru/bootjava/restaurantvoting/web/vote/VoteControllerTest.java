package ru.bootjava.restaurantvoting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.bootjava.restaurantvoting.repository.VoteRepository;
import ru.bootjava.restaurantvoting.web.AbstractControllerTest;
import ru.bootjava.restaurantvoting.web.restaurant.RestaurantTestData;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.bootjava.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static ru.bootjava.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;
import static ru.bootjava.restaurantvoting.web.user.UserTestData.USER_MAIL;
import static ru.bootjava.restaurantvoting.web.vote.VoteTestData.*;

class VoteControllerTest extends AbstractControllerTest {

    private static final String URL = "/votes";
    private static final String URL_SLASH = URL + "/";

    @Autowired
    private VoteRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllVotesByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantTestData.REST_URL_SLASH + RESTAURANT_2_ID + URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote4, vote1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllVotesByRestaurantForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantTestData.REST_URL_SLASH + RESTAURANT_3_ID + URL_SLASH + VOTE_3_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote3));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantTestData.REST_URL_SLASH + RESTAURANT_3_ID + URL_SLASH + VOTE_3_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL_SLASH + VOTE_NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(RestaurantTestData.REST_URL_SLASH + RESTAURANT_3_ID + URL_SLASH + VOTE_3_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.get(RESTAURANT_3_ID, VOTE_3_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(RestaurantTestData.REST_URL_SLASH + RESTAURANT_3_ID + URL_SLASH + VOTE_3_ID))
                .andExpect(status().isForbidden());
    }
}
