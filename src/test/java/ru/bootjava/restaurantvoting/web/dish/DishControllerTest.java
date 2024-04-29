package ru.bootjava.restaurantvoting.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.bootjava.restaurantvoting.model.Dish;
import ru.bootjava.restaurantvoting.repository.DishRepository;
import ru.bootjava.restaurantvoting.util.JsonUtil;
import ru.bootjava.restaurantvoting.web.AbstractControllerTest;
import ru.bootjava.restaurantvoting.web.restaurant.RestaurantTestData;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.bootjava.restaurantvoting.web.dish.DishTestData.*;
import static ru.bootjava.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static ru.bootjava.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;
import static ru.bootjava.restaurantvoting.web.user.UserTestData.USER_MAIL;

class DishControllerTest extends AbstractControllerTest {

    private static final String URL = "/dishes";
    private static final String URL_SLASH = URL + "/";

    @Autowired
    private DishRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1, dish2, dish3));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getTodayMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL_SLASH + "today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1, dish2));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL_SLASH + DISH_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL_SLASH + DishTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL_SLASH + DISH_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.get(RESTAURANT_1_ID, DISH_1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(RestaurantTestData.REST_URL_SLASH + RESTAURANT_2_ID + URL_SLASH + DISH_1_ID))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL_SLASH + DISH_1_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = DishTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isCreated());
        Dish created = DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(repository.getExisted(newId), newDish);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Dish invalidCreated = new Dish(null, "", 0, null);
        perform(MockMvcRequestBuilders.post(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalidCreated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createForbidden() throws Exception {
        perform(MockMvcRequestBuilders.post(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishTestData.getNew())))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL_SLASH + DISH_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(repository.getExisted(DISH_1_ID), DishTestData.getUpdated());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Dish invalidUpdated = new Dish(DISH_1_ID, "", 0, null);
        perform(MockMvcRequestBuilders.put(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL_SLASH + DISH_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalidUpdated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateForbidden() throws Exception {
        perform(MockMvcRequestBuilders.put(RestaurantTestData.REST_URL_SLASH + RESTAURANT_1_ID + URL_SLASH + DISH_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishTestData.getUpdated())))
                .andExpect(status().isForbidden());
    }
}