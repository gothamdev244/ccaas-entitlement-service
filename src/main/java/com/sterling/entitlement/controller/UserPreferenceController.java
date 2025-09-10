package com.sterling.entitlement.controller;

import com.sterling.entitlement.entity.UserLayoutPreferences;
import com.sterling.entitlement.repository.UserPreferenceCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for User Preference Cache operations (Layer 3).
 * 
 * Provides high-performance endpoints for user layout retrieval and cache management.
 */
@RestController
@RequestMapping("/api/v1/data/user-preferences")
public class UserPreferenceController {

    @Autowired
    private UserPreferenceCacheRepository repository;

    /**
     * Get user layout preferences (cached).
     * 
     * This is the primary endpoint for fast user layout retrieval.
     * Returns cached layout if available and not expired.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserLayoutPreferences> getUserPreferences(@PathVariable String userId) {
        Optional<UserLayoutPreferences> preferences = repository.findValidCacheByUserId(userId);
        
        if (preferences.isPresent()) {
            return ResponseEntity.ok(preferences.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create or update user layout preferences.
     */
    @PostMapping
    public ResponseEntity<UserLayoutPreferences> createUserPreferences(@RequestBody UserLayoutPreferences preferences) {
        // Set cache expiry to 4 hours from now
        preferences.setCacheExpiry(LocalDateTime.now().plusHours(4));
        preferences.setComputationSource("api");
        
        UserLayoutPreferences saved = repository.save(preferences);
        return ResponseEntity.ok(saved);
    }

    /**
     * Delete user preferences.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserPreferences(@PathVariable String userId) {
        repository.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete expired preferences (cleanup endpoint).
     */
    @DeleteMapping("/expired")
    public ResponseEntity<String> deleteExpiredPreferences() {
        int deletedCount = repository.deleteExpiredPreferences();
        return ResponseEntity.ok("Deleted " + deletedCount + " expired preferences");
    }

    /**
     * Get cache statistics.
     */
    @GetMapping("/stats")
    public ResponseEntity<Object> getCacheStats() {
        long validCount = repository.countValidCache();
        long expiredCount = repository.countExpiredCache();
        
        return ResponseEntity.ok(new Object() {
            public final long validEntries = validCount;
            public final long expiredEntries = expiredCount;
            public final long totalEntries = validCount + expiredCount;
            public final double hitRatio = validCount > 0 ? (double) validCount / (validCount + expiredCount) * 100 : 0;
        });
    }

    /**
     * Warm cache for multiple users.
     */
    @PostMapping("/warm-cache")
    public ResponseEntity<String> warmCache(@RequestBody List<String> userIds) {
        // This would typically involve calling the layout computation service
        // For now, just return a placeholder response
        return ResponseEntity.ok("Cache warming initiated for " + userIds.size() + " users");
    }

    /**
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<Object> health() {
        return ResponseEntity.ok(new Object() {
            public final String status = "UP";
            public final String service = "UserPreferenceController";
            public final LocalDateTime timestamp = LocalDateTime.now();
        });
    }
}
