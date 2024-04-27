package ru.bootjava.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "meal_date"}, name = "meal_unique_name_date_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant"})
public class Meal extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    private Integer price;

    @Column(name = "meal_date", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Restaurant restaurant;

    public Meal(Integer id, String name, Integer price, LocalDate date) {
        super(id, name);
        this.price = price;
        this.date = date;
    }
}