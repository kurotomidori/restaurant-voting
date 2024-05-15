package ru.bootjava.restaurantvoting.web.vote;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.restaurantvoting.model.Vote;
import ru.bootjava.restaurantvoting.repository.VoteRepository;
import ru.bootjava.restaurantvoting.web.AbstractControllerTest;

import java.time.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.bootjava.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static ru.bootjava.restaurantvoting.web.user.UserTestData.ADMIN_MAIL;
import static ru.bootjava.restaurantvoting.web.user.UserTestData.USER_MAIL;
import static ru.bootjava.restaurantvoting.web.vote.VoteController.REST_URL;
import static ru.bootjava.restaurantvoting.web.vote.VoteTestData.*;

class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + "/";

    @Autowired
    private VoteRepository repository;

    @MockBean
    private Clock clock;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllUserVotes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote4, vote3));
    }

    @Test
    void getAllUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getTodayVote() throws Exception {
        BDDMockito.given(clock.instant()).willReturn(Instant.now());
        BDDMockito.given(clock.getZone()).willReturn(ZoneId.systemDefault());
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote2));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + VOTE_3_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote3));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + VOTE_3_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + VOTE_NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void makeNewVote() throws Exception {
        BDDMockito.given(clock.instant()).willReturn(Instant.parse("2042-01-01T12:00:00Z"));
        BDDMockito.given(clock.getZone()).willReturn(ZoneId.systemDefault());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(RESTAURANT_3_ID)))
                .andDo(print())
                .andExpect(status().isCreated());
        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        Vote newVote = VoteTestData.getNew();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.getExisted(newId), newVote);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void revote() throws Exception {
        BDDMockito.given(clock.instant()).willReturn(LocalDateTime.now().withHour(10).toInstant(ZoneOffset.of("Z")));
        BDDMockito.given(clock.getZone()).willReturn(ZoneId.of("UTC"));
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + VOTE_2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(RESTAURANT_3_ID)))
                .andDo(print())
                .andExpect(status().isNoContent());
        VOTE_MATCHER.assertMatch(repository.getExisted(VOTE_2_ID), getChanged());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void revoteTooLate() throws Exception {
        BDDMockito.given(clock.instant()).willReturn(LocalDateTime.now().withHour(12).toInstant(ZoneOffset.of("Z")));
        BDDMockito.given(clock.getZone()).willReturn(ZoneId.of("UTC"));
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + VOTE_2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(RESTAURANT_3_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
