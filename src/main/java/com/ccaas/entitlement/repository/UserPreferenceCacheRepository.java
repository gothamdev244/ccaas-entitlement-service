package com.ccaas.entitlement.repository;

import com.ccaas.entitlement.entity.UserLayoutPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for UserLayoutPreferences entity operations.
 * 
 * Provides high-performance data access methods for Layer 3 of the entitlement system.
 * Optimized for cache operations and user layout retrieval.
 */
@Repository
public interface UserPreferenceCacheRepository extends JpaRepository<UserLayoutPreferences, String> {

    /**
     * Find valid (non-expired) cache by user ID.
     * This is the primary method for fast user layout retrieval.
     */
    @Query("SELECT ulp FROM UserLayoutPreferences ulp WHERE ulp.userId = :userId AND ulp.cacheExpiry > CURRENT_TIMESTAMP ORDER BY ulp.lastComputedAt DESC")
    Optional<UserLayoutPreferences> findValidCacheByUserId(@Param("userId") String userId);

    /**
     * Find cache by user ID regardless of expiry.
     */
    Optional<UserLayoutPreferences> findByUserId(String userId);

    /**
     * Find all expired cache entries.
     */
    @Query("SELECT ulp FROM UserLayoutPreferences ulp WHERE ulp.cacheExpiry < CURRENT_TIMESTAMP")
    List<UserLayoutPreferences> findExpiredCache();

    /**
     * Delete expired cache entries.
     */
    @Modifying
    @Query("DELETE FROM UserLayoutPreferences ulp WHERE ulp.cacheExpiry < CURRENT_TIMESTAMP")
    int deleteExpiredPreferences();

    /**
     * Find cache entries by computation source.
     */
    List<UserLayoutPreferences> findByComputationSource(String computationSource);

    /**
     * Find cache entries by primary market.
     */
    List<UserLayoutPreferences> findByPrimaryMarket(String primaryMarket);

    /**
     * Find cache entries created after specified time.
     */
    List<UserLayoutPreferences> findByLastComputedAtAfter(LocalDateTime after);

    /**
     * Find cache entries expiring before specified time.
     */
    List<UserLayoutPreferences> findByCacheExpiryBefore(LocalDateTime before);

    /**
     * Count valid (non-expired) cache entries.
     */
    @Query("SELECT COUNT(ulp) FROM UserLayoutPreferences ulp WHERE ulp.cacheExpiry > CURRENT_TIMESTAMP")
    long countValidCache();

    /**
     * Count expired cache entries.
     */
    @Query("SELECT COUNT(ulp) FROM UserLayoutPreferences ulp WHERE ulp.cacheExpiry < CURRENT_TIMESTAMP")
    long countExpiredCache();

    /**
     * Find cache entries by user email.
     */
    Optional<UserLayoutPreferences> findByUserEmail(String userEmail);

    /**
     * Find cache entries by multiple user IDs.
     */
    @Query("SELECT ulp FROM UserLayoutPreferences ulp WHERE ulp.userId IN :userIds AND ulp.cacheExpiry > CURRENT_TIMESTAMP")
    List<UserLayoutPreferences> findValidCacheByUserIds(@Param("userIds") List<String> userIds);

    /**
     * Update cache expiry for specific user.
     */
    @Modifying
    @Query("UPDATE UserLayoutPreferences ulp SET ulp.cacheExpiry = :newExpiry WHERE ulp.userId = :userId")
    int updateCacheExpiry(@Param("userId") String userId, @Param("newExpiry") LocalDateTime newExpiry);

    /**
     * Find cache entries expiring within specified hours.
     */
    @Query("SELECT ulp FROM UserLayoutPreferences ulp WHERE ulp.cacheExpiry BETWEEN CURRENT_TIMESTAMP AND :expiryThreshold")
    List<UserLayoutPreferences> findCacheExpiringWithin(@Param("expiryThreshold") LocalDateTime expiryThreshold);
}
