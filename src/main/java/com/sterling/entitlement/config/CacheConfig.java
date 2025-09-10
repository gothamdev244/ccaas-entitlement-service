package com.sterling.entitlement.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Cache configuration for the Entitlement Service.
 * 
 * Configures Caffeine in-memory cache optimized for:
 * - User layout preferences (4-hour TTL)
 * - Role templates (1-hour TTL)
 * - AD group overrides (30-minute TTL)
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Cache manager for entitlement operations.
     * 
     * Configured with different TTLs for different data types:
     * - userPreferences: 4 hours (matches database cache expiry)
     * - roleTemplates: 1 hour (relatively static data)
     * - adGroupOverrides: 30 minutes (more dynamic data)
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        // User preferences cache - 4 hour TTL, high capacity
        cacheManager.registerCustomCache("userPreferences", 
            Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(Duration.ofHours(4))
                .recordStats()
                .build());
        
        // Role templates cache - 1 hour TTL, medium capacity
        cacheManager.registerCustomCache("roleTemplates", 
            Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofHours(1))
                .recordStats()
                .build());
        
        // AD group overrides cache - 30 minute TTL, high capacity
        cacheManager.registerCustomCache("adGroupOverrides", 
            Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(Duration.ofMinutes(30))
                .recordStats()
                .build());
        
        // Audit cache - 5 minute TTL, low capacity
        cacheManager.registerCustomCache("audit", 
            Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofMinutes(5))
                .recordStats()
                .build());
        
        return cacheManager;
    }
}
