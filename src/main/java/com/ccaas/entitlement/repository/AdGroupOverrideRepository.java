package com.ccaas.entitlement.repository;

import com.ccaas.entitlement.entity.AdGroupLayoutOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for AdGroupLayoutOverride entity operations.
 * 
 * Provides data access methods for Layer 2 of the entitlement system.
 * Optimized for SHA-256 hash-based lookups.
 */
@Repository
public interface AdGroupOverrideRepository extends JpaRepository<AdGroupLayoutOverride, String> {

    /**
     * Find overrides by AD group hash (primary lookup method).
     */
    AdGroupLayoutOverride findByAdGroupHashAndIsActiveTrue(String adGroupHash);

    /**
     * Find overrides by multiple AD group hashes.
     */
    @Query("SELECT ago FROM AdGroupLayoutOverride ago WHERE ago.adGroupHash IN :hashes AND ago.isActive = true ORDER BY ago.priority ASC")
    List<AdGroupLayoutOverride> findByAdGroupHashInAndIsActiveTrue(@Param("hashes") List<String> hashes);

    /**
     * Find overrides by AD group DN.
     */
    AdGroupLayoutOverride findByAdGroupDnAndIsActiveTrue(String adGroupDn);

    /**
     * Find overrides by multiple AD group DNs.
     */
    @Query("SELECT ago FROM AdGroupLayoutOverride ago WHERE ago.adGroupDn IN :dns AND ago.isActive = true ORDER BY ago.priority ASC")
    List<AdGroupLayoutOverride> findByAdGroupDnInAndIsActiveTrue(@Param("dns") List<String> dns);

    /**
     * Find overrides by parsed market.
     */
    List<AdGroupLayoutOverride> findByParsedMarketAndIsActiveTrueOrderByPriorityAsc(String parsedMarket);

    /**
     * Find overrides by parsed function.
     */
    List<AdGroupLayoutOverride> findByParsedFunctionAndIsActiveTrueOrderByPriorityAsc(String parsedFunction);

    /**
     * Find overrides by parsed environment.
     */
    List<AdGroupLayoutOverride> findByParsedEnvironmentAndIsActiveTrueOrderByPriorityAsc(String parsedEnvironment);

    /**
     * Find overrides by market and function.
     */
    @Query("SELECT ago FROM AdGroupLayoutOverride ago WHERE ago.parsedMarket = :market AND ago.parsedFunction = :function AND ago.isActive = true ORDER BY ago.priority ASC")
    List<AdGroupLayoutOverride> findByMarketAndFunctionAndActive(@Param("market") String market, @Param("function") String function);

    /**
     * Find overrides by market and environment.
     */
    @Query("SELECT ago FROM AdGroupLayoutOverride ago WHERE ago.parsedMarket = :market AND ago.parsedEnvironment = :environment AND ago.isActive = true ORDER BY ago.priority ASC")
    List<AdGroupLayoutOverride> findByMarketAndEnvironmentAndActive(@Param("market") String market, @Param("environment") String environment);

    /**
     * Find all active overrides ordered by priority.
     */
    List<AdGroupLayoutOverride> findByIsActiveTrueOrderByPriorityAsc();

    /**
     * Count active overrides by market.
     */
    long countByParsedMarketAndIsActiveTrue(String parsedMarket);

    /**
     * Find overrides with priority less than specified value.
     */
    List<AdGroupLayoutOverride> findByPriorityLessThanAndIsActiveTrueOrderByPriorityAsc(Integer priority);
}
