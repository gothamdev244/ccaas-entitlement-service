package com.ccaas.entitlement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot application for the Entitlement Service.
 * 
 * This service provides high-performance data access for the 3-layer entitlement architecture:
 * - Layer 1: Role Layout Templates
 * - Layer 2: AD Group Granular Overrides  
 * - Layer 3: User Preference Cache
 * 
 * Features:
 * - Java 21 with Virtual Threads
 * - PostgreSQL with JSONB support
 * - In-memory Caffeine caching
 * - Spring Boot 3.x with Jakarta EE
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableCaching
@EnableScheduling
public class EntitlementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntitlementServiceApplication.class, args);
    }
}
