package com.sterling.entitlement.controller;

import com.sterling.entitlement.entity.LayoutComputationAudit;
import com.sterling.entitlement.repository.LayoutComputationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Layout Computation Audit operations.
 * 
 * Provides endpoints for audit trail and performance monitoring.
 */
@RestController
@RequestMapping("/api/v1/data/audit")
public class AuditController {

    @Autowired
    private LayoutComputationRepository repository;

    /**
     * Get audit entries for a user.
     */
    @GetMapping("/computation/{userId}")
    public ResponseEntity<List<LayoutComputationAudit>> getAuditByUser(@PathVariable String userId) {
        List<LayoutComputationAudit> audits = repository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(audits);
    }

    /**
     * Get audit entries for a user within date range.
     */
    @GetMapping("/computation/{userId}/range")
    public ResponseEntity<List<LayoutComputationAudit>> getAuditByUserAndDateRange(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        
        List<LayoutComputationAudit> audits = repository.findByUserIdAndDateRange(userId, from, to);
        return ResponseEntity.ok(audits);
    }

    /**
     * Get performance statistics.
     */
    @GetMapping("/computation/performance-stats")
    public ResponseEntity<Object> getPerformanceStats() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        Object[] stats = repository.getPerformanceStats(oneHourAgo);
        
        if (stats != null && stats.length >= 6) {
            return ResponseEntity.ok(new Object() {
                public final Long totalRequests = (Long) stats[0];
                public final Double avgComputationTime = (Double) stats[1];
                public final Long maxComputationTime = (Long) stats[2];
                public final Long minComputationTime = (Long) stats[3];
                public final Long cacheHits = (Long) stats[4];
                public final Long cacheMisses = (Long) stats[5];
                public final Double cacheHitRatio = cacheHits > 0 ? (double) cacheHits / (cacheHits + cacheMisses) * 100 : 0.0;
            });
        } else {
            return ResponseEntity.ok(new Object() {
                public final String message = "No performance data available";
            });
        }
    }

    /**
     * Get cache hit ratio.
     */
    @GetMapping("/computation/cache-hit-ratio")
    public ResponseEntity<Object> getCacheHitRatio() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        Double hitRatio = repository.getCacheHitRatio(oneHourAgo);
        
        return ResponseEntity.ok(new Object() {
            public final Double cacheHitRatio = hitRatio != null ? hitRatio : 0.0;
            public final String period = "last hour";
        });
    }

    /**
     * Get slowest computations.
     */
    @GetMapping("/computation/slowest")
    public ResponseEntity<List<LayoutComputationAudit>> getSlowestComputations() {
        List<LayoutComputationAudit> slowest = repository.findSlowestComputations();
        return ResponseEntity.ok(slowest);
    }

    /**
     * Get audit entries by cache status.
     */
    @GetMapping("/computation/cache-status/{status}")
    public ResponseEntity<List<LayoutComputationAudit>> getAuditByCacheStatus(@PathVariable String status) {
        List<LayoutComputationAudit> audits = repository.findByCacheStatusOrderByCreatedAtDesc(status);
        return ResponseEntity.ok(audits);
    }

    /**
     * Get audit entries by computation source.
     */
    @GetMapping("/computation/source/{source}")
    public ResponseEntity<List<LayoutComputationAudit>> getAuditBySource(@PathVariable String source) {
        List<LayoutComputationAudit> audits = repository.findByComputationSourceOrderByCreatedAtDesc(source);
        return ResponseEntity.ok(audits);
    }

    /**
     * Get audit count by user.
     */
    @GetMapping("/computation/{userId}/count")
    public ResponseEntity<Object> getAuditCountByUser(@PathVariable String userId) {
        long count = repository.countByUserId(userId);
        return ResponseEntity.ok(new Object() {
            public final String userIdValue = userId;
            public final long auditCount = count;
        });
    }

    /**
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<Object> health() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        long recentAudits = repository.findByCreatedAtAfterOrderByCreatedAtDesc(oneHourAgo).size();
        
        return ResponseEntity.ok(new Object() {
            public final String status = "UP";
            public final String service = "AuditController";
            public final long recentAuditsCount = recentAudits;
        });
    }
}
