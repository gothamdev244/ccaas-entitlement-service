package com.ccaas.entitlement.controller;

import com.ccaas.entitlement.entity.AdGroupLayoutOverride;
import com.ccaas.entitlement.repository.AdGroupOverrideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for AD Group Override operations (Layer 2).
 * 
 * Provides endpoints for managing AD group-specific layout overrides.
 */
@RestController
@RequestMapping("/api/v1/data/ad-group-overrides")
public class AdGroupOverrideController {

    @Autowired
    private AdGroupOverrideRepository repository;

    /**
     * Get all active AD group overrides.
     */
    @GetMapping
    public ResponseEntity<List<AdGroupLayoutOverride>> getAllAdGroupOverrides() {
        List<AdGroupLayoutOverride> overrides = repository.findByIsActiveTrueOrderByPriorityAsc();
        return ResponseEntity.ok(overrides);
    }

    /**
     * Get AD group override by hash.
     */
    @GetMapping("/{groupHash}")
    public ResponseEntity<AdGroupLayoutOverride> getAdGroupOverride(@PathVariable String groupHash) {
        AdGroupLayoutOverride override = repository.findByAdGroupHashAndIsActiveTrue(groupHash);
        
        if (override != null) {
            return ResponseEntity.ok(override);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create new AD group override.
     */
    @PostMapping
    public ResponseEntity<AdGroupLayoutOverride> createAdGroupOverride(@RequestBody AdGroupLayoutOverride override) {
        override.setIsActive(true);
        
        AdGroupLayoutOverride saved = repository.save(override);
        return ResponseEntity.ok(saved);
    }

    /**
     * Update AD group override.
     */
    @PutMapping("/{groupHash}")
    public ResponseEntity<AdGroupLayoutOverride> updateAdGroupOverride(@PathVariable String groupHash, 
                                                                      @RequestBody AdGroupLayoutOverride override) {
        AdGroupLayoutOverride existing = repository.findByAdGroupHashAndIsActiveTrue(groupHash);
        
        if (existing != null) {
            override.setAdGroupHash(groupHash);
            override.setIsActive(true);
            AdGroupLayoutOverride saved = repository.save(override);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete AD group override (soft delete).
     */
    @DeleteMapping("/{groupHash}")
    public ResponseEntity<Void> deleteAdGroupOverride(@PathVariable String groupHash) {
        AdGroupLayoutOverride override = repository.findByAdGroupHashAndIsActiveTrue(groupHash);
        
        if (override != null) {
            override.setIsActive(false);
            repository.save(override);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get overrides by market.
     */
    @GetMapping("/market/{market}")
    public ResponseEntity<List<AdGroupLayoutOverride>> getOverridesByMarket(@PathVariable String market) {
        List<AdGroupLayoutOverride> overrides = repository.findByParsedMarketAndIsActiveTrueOrderByPriorityAsc(market);
        return ResponseEntity.ok(overrides);
    }

    /**
     * Get overrides by function.
     */
    @GetMapping("/function/{function}")
    public ResponseEntity<List<AdGroupLayoutOverride>> getOverridesByFunction(@PathVariable String function) {
        List<AdGroupLayoutOverride> overrides = repository.findByParsedFunctionAndIsActiveTrueOrderByPriorityAsc(function);
        return ResponseEntity.ok(overrides);
    }

    /**
     * Get overrides by environment.
     */
    @GetMapping("/environment/{environment}")
    public ResponseEntity<List<AdGroupLayoutOverride>> getOverridesByEnvironment(@PathVariable String environment) {
        List<AdGroupLayoutOverride> overrides = repository.findByParsedEnvironmentAndIsActiveTrueOrderByPriorityAsc(environment);
        return ResponseEntity.ok(overrides);
    }

    /**
     * Bulk lookup by AD group DNs.
     */
    @PostMapping("/bulk-lookup")
    public ResponseEntity<Object> bulkLookup(@RequestBody List<String> adGroupDns) {
        List<AdGroupLayoutOverride> overrides = repository.findByAdGroupDnInAndIsActiveTrue(adGroupDns);
        
        return ResponseEntity.ok(new Object() {
            public final List<AdGroupLayoutOverride> overridesList = overrides;
            public final int foundCount = overrides.size();
            public final int requestedCount = adGroupDns.size();
        });
    }

    /**
     * Get override count by market.
     */
    @GetMapping("/count/market/{market}")
    public ResponseEntity<Object> getOverrideCountByMarket(@PathVariable String market) {
        long count = repository.countByParsedMarketAndIsActiveTrue(market);
        return ResponseEntity.ok(new Object() {
            public final String marketName = market;
            public final long overrideCount = count;
        });
    }

    /**
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<Object> health() {
        return ResponseEntity.ok(new Object() {
            public final String status = "UP";
            public final String service = "AdGroupOverrideController";
            public final long activeOverrides = repository.findByIsActiveTrueOrderByPriorityAsc().size();
        });
    }
}
