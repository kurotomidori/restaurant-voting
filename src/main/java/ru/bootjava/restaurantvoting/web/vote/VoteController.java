package ru.bootjava.restaurantvoting.web.vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bootjava.restaurantvoting.model.Vote;
import ru.bootjava.restaurantvoting.repository.VoteRepository;

import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class VoteController {

    static final String REST_URL = "/api/restaurants/{rest_id}/votes";

    private final VoteRepository repository;

    @GetMapping
    public List<Vote> getAllVotesByRestaurant(@PathVariable(name = "rest_id") int restaurantId) {
        log.info("get all Votes for Restaurant {}", restaurantId);
        return repository.getAllVotesByRestaurant(restaurantId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vote> get(@PathVariable(name = "rest_id") int restaurantId, @PathVariable int id) {
        log.info("get Vote {} of Restaurant {}", id, restaurantId);
        return ResponseEntity.of(repository.get(restaurantId, id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "rest_id") int restaurantId, @PathVariable int id) {
        log.info("delete Vote {} for Restaurant {}", id, restaurantId);
        Vote vote = repository.getBelonged(restaurantId, id);
        repository.delete(vote);
    }
}
