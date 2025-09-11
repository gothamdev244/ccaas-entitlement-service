package com.ccaas.entitlement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot application for the CCAAS Entitlement Computation Service.
 * 
 * This service provides high-performance data access for entitlement computation:
 * - Role Layout Templates
 * - AD Group Granular Overrides  
 * - User Preference Cache
 * - Layout Computation Engine
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
public class EntitlementComputationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntitlementComputationServiceApplication.class, args);
    }
}
