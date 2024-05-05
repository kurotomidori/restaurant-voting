package ru.bootjava.restaurantvoting.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@AllArgsConstructor
@Profile("!test")
public class CacheConfiguration {

    CacheProperties cacheProperties;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCache userCache = buildCache(cacheProperties.getUsersCacheName(),
                cacheProperties.getUsersCacheTTL(), cacheProperties.getUsersCacheMaximumSize());
        CaffeineCache dishesCache = buildCache(cacheProperties.getDishesCacheName(),
                cacheProperties.getDishesCacheTTL(), cacheProperties.getDishesCacheMaximumSize());
        CaffeineCache restaurantsCache = buildCache(cacheProperties.getRestaurantsCacheName(),
                cacheProperties.getRestaurantsCacheTTL(), cacheProperties.getRestaurantsCacheMaximumSize());
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(userCache, dishesCache, restaurantsCache));
        return manager;
    }

    private CaffeineCache buildCache(String name, int minutesToExpire, int maximumSize) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(minutesToExpire, TimeUnit.MINUTES)
                .maximumSize(maximumSize)
                .build());
    }

}
