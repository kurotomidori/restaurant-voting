package ru.bootjava.restaurantvoting.web.vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.bootjava.restaurantvoting.model.Vote;
import ru.bootjava.restaurantvoting.repository.VoteRepository;
import ru.bootjava.restaurantvoting.service.VoteService;
import ru.bootjava.restaurantvoting.web.AuthUser;

import java.net.URI;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class VoteController {

    static final String REST_URL = "/api/profile/votes";

    private final VoteRepository repository;
    private final VoteService service;
    private final Clock clock;

    @GetMapping
    List<Vote> getAllUserVotes(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get votes of User {}", authUser.id());
        return repository.getAllVotesByUser(authUser.id());
    }

    @GetMapping("/today")
    public ResponseEntity<Vote> getTodayVote(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get today's vote of User {}", authUser.id());
        return ResponseEntity.of(repository.getVoteByUserByDate(authUser.id(), LocalDate.now(clock)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vote> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get Vote {} of User {}", id, authUser.id());
        return ResponseEntity.of(repository.get(authUser.id(), id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestBody int restaurantId) {
        log.info("User {} vote for Restaurant {}", authUser.id(), restaurantId);
        Vote created = service.save(authUser.id(), restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeVote(@PathVariable int id, @AuthenticationPrincipal AuthUser authUser, @RequestBody int restaurantId) {
        log.info("User {} change vote {} for Restaurant {}", authUser.id(), id, restaurantId);
        service.changeVote(id, authUser.id(), restaurantId);
    }
}
