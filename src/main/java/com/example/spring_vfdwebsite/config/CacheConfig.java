package com.example.spring_vfdwebsite.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        // Có thể thêm các cache names nếu cần
        cacheManager.setCacheNames(Arrays.asList("users", "board-directors", "affected-objects", "partners", "heroes", "documents", "projects", "galleries"));
        return cacheManager;
    }
}
