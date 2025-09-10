package com.sterling.entitlement.repository;

import com.sterling.entitlement.entity.LayoutComputationAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for LayoutComputationAudit entity operations.
 * 
 * Provides data access methods for audit trail and performance monitoring.
 */
@Repository
public interface LayoutComputationRepository extends JpaRepository<LayoutComputationAudit, Long> {

    /**
     * Find audit entries by user ID.
     */
    List<LayoutComputationAudit> findByUserIdOrderByCreatedAtDesc(String userId);

    /**
     * Find audit entries by user ID within date range.
     */
    @Query("SELECT lca FROM LayoutComputationAudit lca WHERE lca.userId = :userId AND lca.createdAt BETWEEN :fromDate AND :toDate ORDER BY lca.createdAt DESC")
    List<LayoutComputationAudit> findByUserIdAndDateRange(@Param("userId") String userId, 
                                                         @Param("fromDate") LocalDateTime fromDate, 
                                                         @Param("toDate") LocalDateTime toDate);

    /**
     * Find audit entries by cache status.
     */
    List<LayoutComputationAudit> findByCacheStatusOrderByCreatedAtDesc(String cacheStatus);

    /**
     * Find audit entries by computation source.
     */
    List<LayoutComputationAudit> findByComputationSourceOrderByCreatedAtDesc(String computationSource);

    /**
     * Find audit entries created after specified time.
     */
    List<LayoutComputationAudit> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime after);

    /**
     * Find audit entries with computation time greater than specified milliseconds.
     */
    List<LayoutComputationAudit> findByComputationTimeMsGreaterThanOrderByComputationTimeMsDesc(Long computationTimeMs);

    /**
     * Get performance statistics for the last hour.
     */
    @Query("SELECT " +
           "COUNT(lca) as totalRequests, " +
           "AVG(lca.computationTimeMs) as avgComputationTime, " +
           "MAX(lca.computationTimeMs) as maxComputationTime, " +
           "MIN(lca.computationTimeMs) as minComputationTime, " +
           "SUM(CASE WHEN lca.cacheStatus = 'hit' THEN 1 ELSE 0 END) as cacheHits, " +
           "SUM(CASE WHEN lca.cacheStatus = 'miss' THEN 1 ELSE 0 END) as cacheMisses " +
           "FROM LayoutComputationAudit lca " +
           "WHERE lca.createdAt > :since")
    Object[] getPerformanceStats(@Param("since") LocalDateTime since);

    /**
     * Get cache hit ratio for the last hour.
     */
    @Query("SELECT " +
           "CAST(SUM(CASE WHEN lca.cacheStatus = 'hit' THEN 1 ELSE 0 END) AS DOUBLE) / COUNT(lca) * 100 " +
           "FROM LayoutComputationAudit lca " +
           "WHERE lca.createdAt > :since")
    Double getCacheHitRatio(@Param("since") LocalDateTime since);

    /**
     * Find slowest computations (top 10).
     */
    @Query("SELECT lca FROM LayoutComputationAudit lca ORDER BY lca.computationTimeMs DESC LIMIT 10")
    List<LayoutComputationAudit> findSlowestComputations();

    /**
     * Count audit entries by user ID.
     */
    long countByUserId(String userId);

    /**
     * Count audit entries by cache status.
     */
    long countByCacheStatus(String cacheStatus);

    /**
     * Delete audit entries older than specified date.
     */
    @Query("DELETE FROM LayoutComputationAudit lca WHERE lca.createdAt < :beforeDate")
    int deleteAuditEntriesOlderThan(@Param("beforeDate") LocalDateTime beforeDate);

    /**
     * Find audit entries by user email.
     */
    List<LayoutComputationAudit> findByUserEmailOrderByCreatedAtDesc(String userEmail);

    /**
     * Get computation time percentiles for performance analysis.
     */
    @Query(value = "SELECT " +
                   "PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY computation_time_ms) as p50, " +
                   "PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY computation_time_ms) as p95, " +
                   "PERCENTILE_CONT(0.99) WITHIN GROUP (ORDER BY computation_time_ms) as p99 " +
                   "FROM layout_computation_audit " +
                   "WHERE created_at > :since", nativeQuery = true)
    Object[] getComputationTimePercentiles(@Param("since") LocalDateTime since);
}
