package com.ccaas.entitlement.repository;

import com.ccaas.entitlement.entity.LayoutComputationAudit;
import com.ccaas.entitlement.entity.UserLayoutPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Repository for cache maintenance operations.
 * 
 * Provides methods for cache cleanup, metrics, and maintenance tasks.
 */
@Repository
public interface CacheMaintenanceRepository extends JpaRepository<UserLayoutPreferences, String> {

    /**
     * Delete expired user preferences.
     */
    @Modifying
    @Query("DELETE FROM UserLayoutPreferences ulp WHERE ulp.cacheExpiry < CURRENT_TIMESTAMP")
    int deleteExpiredPreferences();

    /**
     * Count expired preferences.
     */
    @Query("SELECT COUNT(ulp) FROM UserLayoutPreferences ulp WHERE ulp.cacheExpiry < CURRENT_TIMESTAMP")
    long countExpiredPreferences();

    /**
     * Count valid (non-expired) preferences.
     */
    @Query("SELECT COUNT(ulp) FROM UserLayoutPreferences ulp WHERE ulp.cacheExpiry > CURRENT_TIMESTAMP")
    long countValidPreferences();

    /**
     * Count active user count.
     */
    @Query("SELECT COUNT(DISTINCT ulp.userId) FROM UserLayoutPreferences ulp WHERE ulp.cacheExpiry > CURRENT_TIMESTAMP")
    long getActiveUserCount();
}
