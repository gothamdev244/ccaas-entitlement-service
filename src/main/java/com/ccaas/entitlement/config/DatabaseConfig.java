package com.ccaas.entitlement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Database configuration for the Entitlement Service.
 * 
 * Configures PostgreSQL connection with HikariCP connection pooling
 * optimized for high-performance entitlement operations.
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    /**
     * HikariCP DataSource configuration optimized for entitlement operations.
     * 
     * Configuration tuned for:
     * - High concurrency (50 max connections)
     * - Fast connection acquisition (2s timeout)
     * - Efficient connection reuse
     * - Prepared statement caching
     */
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        
        // Set the JDBC URL from application properties
        config.setJdbcUrl("jdbc:postgresql://localhost:5434/sterling_platform");
        config.setUsername("sterling_user");
        config.setPassword("sterling_password");
        config.setDriverClassName("org.postgresql.Driver");
        
        // Connection pool settings optimized for entitlement service
        config.setMinimumIdle(10);                    // Higher minimum for layout queries
        config.setMaximumPoolSize(50);                // Higher maximum for concurrent users
        config.setConnectionTimeout(2000);             // Fast timeout for UI responsiveness
        config.setIdleTimeout(30000);                 // 30 seconds idle timeout
        config.setMaxLifetime(120000);                // 2 minutes max lifetime
        config.setLeakDetectionThreshold(60000);      // 1 minute leak detection
        
        // Prepared statement caching for performance
        config.addDataSourceProperty("preparedStatementCacheSize", "100");
        config.addDataSourceProperty("preparedStatementCacheSqlLimit", "2048");
        
        // Connection validation
        config.setConnectionTestQuery("SELECT 1");
        config.setValidationTimeout(1000);
        
        // Performance optimizations
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        
        return new HikariDataSource(config);
    }
}
