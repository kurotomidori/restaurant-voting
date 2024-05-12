package ru.bootjava.restaurantvoting.web.dish;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.bootjava.restaurantvoting.model.Dish;
import ru.bootjava.restaurantvoting.repository.DishRepository;
import ru.bootjava.restaurantvoting.service.DishService;

import java.net.URI;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static ru.bootjava.restaurantvoting.web.RestValidation.assureIdConsistent;
import static ru.bootjava.restaurantvoting.web.RestValidation.checkNew;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class DishController {

    static final String REST_URL = "/api/restaurants/{restId}/dishes";

    private final DishRepository repository;
    private final DishService service;
    private final Clock clock;

    @GetMapping
    public List<Dish> getAll(@PathVariable(name = "restId") int restaurantId) {
        log.info("get all Dishes for Restaurant {}", restaurantId);
        return repository.getAll(restaurantId);
    }

    @GetMapping("/today")
    public List<Dish> getTodayMenu(@PathVariable(name = "restId") int restaurantId) {
        log.info("get all today's Dishes for Restaurant {}", restaurantId);
        return repository.getDishesByDate(restaurantId, LocalDate.now(clock));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@PathVariable(name = "restId") int restaurantId, @PathVariable int id) {
        log.info("get Dish {} of Restaurant {}", id, restaurantId);
        return ResponseEntity.of(repository.get(restaurantId, id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "restId") int restaurantId, @PathVariable int id) {
        log.info("delete Dish {} for Restaurant {}", id, restaurantId);
        Dish dish = repository.getBelonged(restaurantId, id);
        repository.delete(dish);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@PathVariable(name = "restId") int restaurantId, @Valid @RequestBody Dish dish) {
        log.info("create {} for Restaurant {}", dish, restaurantId);
        checkNew(dish);
        Dish created = service.save(restaurantId, dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable(name = "restId") int restaurantId, @Valid @RequestBody Dish dish, @PathVariable int id) {
        log.info("update {} with id={} for Restaurant {}", dish, id, restaurantId);
        assureIdConsistent(dish, id);
        repository.getBelonged(restaurantId, id);
        service.save(restaurantId, dish);
    }
}
