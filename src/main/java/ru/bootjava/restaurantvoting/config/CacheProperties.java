package ru.bootjava.restaurantvoting.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "cache")
@EnableConfigurationProperties
@Data
public class CacheProperties {

    private Map<String, CacheSpec> specs = new HashMap<>();

    @Data
    public static class CacheSpec {
        private int cacheTimeoutInMinutes;
        private int cacheMaximumSize;
    }
}
