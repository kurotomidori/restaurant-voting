package ru.bootjava.restaurantvoting.web.user;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.bootjava.restaurantvoting.model.User;
import ru.bootjava.restaurantvoting.model.Vote;
import ru.bootjava.restaurantvoting.repository.UserRepository;
import ru.bootjava.restaurantvoting.repository.VoteRepository;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractUserController {
    protected final Logger log = getLogger(getClass());

    @Autowired
    protected UserRepository repository;

    @Autowired
    protected VoteRepository voteRepository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public User get(int id) {
        log.info("get {}", id);
        return repository.getExisted(id);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    public List<Vote> getVotesOfUser(int id) {
        log.info("get votes of User {}", id);
        return voteRepository.getAllVotesByUser(id);
    }
}