package ru.bootjava.restaurantvoting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.bootjava.restaurantvoting.error.DataConflictException;
import ru.bootjava.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.date DESC")
    List<Vote> getAllVotesByUser(int userId);

    @Query("SELECT v FROM Vote v WHERE v.id = :id and v.user.id=:userId")
    Optional<Vote> get(int userId, int id);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId and v.date=:date")
    Optional<Vote> getVoteByUserByDate(int userId, LocalDate date);

    default Vote getBelonged(int userId, int id) {
        return get(userId, id).orElseThrow(
                () -> new DataConflictException("Vote id=" + id + "   is not exist or doesn't belong to User id=" + userId));
    }
}