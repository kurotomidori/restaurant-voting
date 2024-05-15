package ru.bootjava.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "vote_date"}, name = "vote_unique_user_date_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant", "user"})
public class Vote extends BaseEntity {

    @Column(name = "vote_date", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Restaurant restaurant;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @Column(name = "restaurant_id", insertable = false, updatable = false)
    private Integer restaurantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @Column(name = "user_id", insertable = false, updatable = false)
    private Integer userId;

    public Vote(Integer id, LocalDate date) {
        super(id);
        this.date = date;
    }

    public Vote(Integer id, LocalDate date, Integer restaurantId, Integer userId) {
        super(id);
        this.date = date;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }
}