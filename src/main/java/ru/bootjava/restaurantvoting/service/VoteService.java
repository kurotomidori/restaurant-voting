package ru.bootjava.restaurantvoting.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.restaurantvoting.model.Vote;
import ru.bootjava.restaurantvoting.repository.RestaurantRepository;
import ru.bootjava.restaurantvoting.repository.UserRepository;
import ru.bootjava.restaurantvoting.repository.VoteRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    private final Clock clock;

    private final LocalTime revotingTimeLimit = LocalTime.of(11, 0);

    @Transactional
    public Vote save(int userId, int restaurantId) {
        Vote vote = new Vote(null, LocalDate.now(clock));
        vote.setUser(userRepository.getExisted(userId));
        vote.setRestaurant(restaurantRepository.getExisted(restaurantId));
        vote.setUserId(userId);
        vote.setRestaurantId(restaurantId);
        return voteRepository.save(vote);
    }

    @Transactional
    public void changeVote(int id, int userId, int restaurantId) {
        LocalDateTime now = LocalDateTime.now(clock);
        if (now.toLocalTime().isAfter(revotingTimeLimit)) {
            throw new IllegalArgumentException("It's to late to vote today");
        }
        Vote vote = voteRepository.getBelonged(userId, id);
        if (!vote.getDate().isEqual(now.toLocalDate())) {
            throw new IllegalArgumentException("You can't change old votes");
        }
        vote.setRestaurant(restaurantRepository.getExisted(restaurantId));
    }
}
