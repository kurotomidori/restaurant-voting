package ru.bootjava.restaurantvoting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    boolean isVoted;

    public RestaurantTo(Integer id, String name, boolean isVoted) {
        super(id, name);
        this.isVoted = isVoted;
    }
}
