package com.sterling.entitlement.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * Entity representing layout computation audit trail.
 * 
 * Tracks how user layouts are computed for troubleshooting and compliance.
 * Provides full audit trail for entitlement decisions.
 */
@Entity
@Table(name = "layout_computation_audit")
public class LayoutComputationAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    @Column(name = "user_id", length = 255, nullable = false)
    private String userId;

    @Column(name = "user_email", length = 255)
    private String userEmail;

    @Column(name = "ad_group_dns", columnDefinition = "text[]")
    private String[] adGroupDns;

    @Column(name = "matched_overrides", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String matchedOverrides;

    @Column(name = "base_roles", columnDefinition = "text[]", nullable = false)
    private String[] baseRoles;

    @Column(name = "computation_steps", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String computationSteps;

    @Column(name = "conflict_resolutions", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String conflictResolutions;

    @Column(name = "final_layout", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String finalLayout;

    @Column(name = "computation_time_ms", nullable = false)
    private Long computationTimeMs;

    @Column(name = "cache_status", length = 20, nullable = false)
    private String cacheStatus; // 'hit', 'miss', 'expired'

    @Column(name = "computation_source", length = 50)
    private String computationSource; // 'database_function', 'service_logic', 'fallback'

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public LayoutComputationAudit() {}

    public LayoutComputationAudit(String userId, String[] adGroupDns, String[] baseRoles, 
                                 Long computationTimeMs, String cacheStatus) {
        this.userId = userId;
        this.adGroupDns = adGroupDns;
        this.baseRoles = baseRoles;
        this.computationTimeMs = computationTimeMs;
        this.cacheStatus = cacheStatus;
    }

    // Getters and Setters
    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String[] getAdGroupDns() {
        return adGroupDns;
    }

    public void setAdGroupDns(String[] adGroupDns) {
        this.adGroupDns = adGroupDns;
    }

    public String getMatchedOverrides() {
        return matchedOverrides;
    }

    public void setMatchedOverrides(String matchedOverrides) {
        this.matchedOverrides = matchedOverrides;
    }

    public String[] getBaseRoles() {
        return baseRoles;
    }

    public void setBaseRoles(String[] baseRoles) {
        this.baseRoles = baseRoles;
    }

    public String getComputationSteps() {
        return computationSteps;
    }

    public void setComputationSteps(String computationSteps) {
        this.computationSteps = computationSteps;
    }

    public String getConflictResolutions() {
        return conflictResolutions;
    }

    public void setConflictResolutions(String conflictResolutions) {
        this.conflictResolutions = conflictResolutions;
    }

    public String getFinalLayout() {
        return finalLayout;
    }

    public void setFinalLayout(String finalLayout) {
        this.finalLayout = finalLayout;
    }

    public Long getComputationTimeMs() {
        return computationTimeMs;
    }

    public void setComputationTimeMs(Long computationTimeMs) {
        this.computationTimeMs = computationTimeMs;
    }

    public String getCacheStatus() {
        return cacheStatus;
    }

    public void setCacheStatus(String cacheStatus) {
        this.cacheStatus = cacheStatus;
    }

    public String getComputationSource() {
        return computationSource;
    }

    public void setComputationSource(String computationSource) {
        this.computationSource = computationSource;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "LayoutComputationAudit{" +
                "auditId=" + auditId +
                ", userId='" + userId + '\'' +
                ", computationTimeMs=" + computationTimeMs +
                ", cacheStatus='" + cacheStatus + '\'' +
                ", computationSource='" + computationSource + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
