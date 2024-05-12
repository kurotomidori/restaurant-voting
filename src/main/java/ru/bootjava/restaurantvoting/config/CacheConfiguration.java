package ru.bootjava.restaurantvoting.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@AllArgsConstructor
@EnableCaching
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "caffeine")
public class CacheConfiguration {

    private CacheProperties cacheProperties;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        Map<String, CacheProperties.CacheSpec> specs = cacheProperties.getSpecs();
        specs.keySet().forEach(cacheName -> {
            CacheProperties.CacheSpec spec = specs.get(cacheName);
            manager.registerCustomCache(cacheName, buildCache(spec));
        });
        return manager;
    }

    private Cache<Object, Object> buildCache(CacheProperties.CacheSpec cacheSpec) {
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheSpec.getCacheTimeoutInMinutes(), TimeUnit.MINUTES)
                .maximumSize(cacheSpec.getCacheMaximumSize())
                .build();
    }
}
