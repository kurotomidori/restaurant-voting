package ru.bootjava.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.restaurantvoting.model.Vote;
import ru.bootjava.restaurantvoting.repository.RestaurantRepository;
import ru.bootjava.restaurantvoting.repository.UserRepository;
import ru.bootjava.restaurantvoting.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Transactional
    public void save(int userId, int restaurantId) {
        if (LocalTime.now().isAfter(LocalTime.of(22, 0))) {
            throw new IllegalArgumentException("It's to late to vote today");
        }
        Optional<Vote> optionalVote = voteRepository.getTodayVoteByUser(userId, LocalDate.now());
        Vote vote;
        if (optionalVote.isEmpty()) {
            vote = new Vote(null, LocalDate.now());
            vote.setUser(userRepository.getExisted(userId));
            vote.setRestaurant(restaurantRepository.getExisted(restaurantId));
        } else {
            vote = optionalVote.get();
            vote.setRestaurant(restaurantRepository.getExisted(restaurantId));
        }
        voteRepository.save(vote);
    }
}
