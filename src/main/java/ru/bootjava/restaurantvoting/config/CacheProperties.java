package ru.bootjava.restaurantvoting.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cache")
@Getter
@Setter
public class CacheProperties {

    private String usersCacheName;

    private int usersCacheMaximumSize;

    private int usersCacheTTL;

    private String dishesCacheName;

    private int dishesCacheMaximumSize;

    private int dishesCacheTTL;

    private String restaurantsCacheName;

    private int restaurantsCacheMaximumSize;

    private int restaurantsCacheTTL;

}
