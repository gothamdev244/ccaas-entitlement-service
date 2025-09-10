package com.ccaas.entitlement.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * Entity representing user layout preferences cache (Layer 3 of entitlement system).
 * 
 * Stores pre-computed final UI configurations for instant loading.
 * This provides the highest performance for user layout retrieval.
 */
@Entity
@Table(name = "user_layout_preferences")
public class UserLayoutPreferences {

    @Id
    @Column(name = "user_id", length = 255)
    private String userId;

    @Column(name = "user_email", length = 255)
    private String userEmail;

    @Column(name = "computed_layout", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String computedLayout;

    @Column(name = "market_theme", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String marketTheme;

    @Column(name = "effective_permissions", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String effectivePermissions;

    @Column(name = "primary_market", length = 10)
    private String primaryMarket;

    @Column(name = "base_roles", columnDefinition = "text[]")
    private String[] baseRoles;

    @Column(name = "cache_expiry", nullable = false)
    private LocalDateTime cacheExpiry;

    @Column(name = "last_computed_at", nullable = false)
    private LocalDateTime lastComputedAt;

    @Column(name = "computation_source", length = 50)
    private String computationSource; // 'cache', 'computation', 'fallback'

    @PrePersist
    protected void onCreate() {
        lastComputedAt = LocalDateTime.now();
        if (cacheExpiry == null) {
            cacheExpiry = LocalDateTime.now().plusHours(4); // 4-hour cache expiry
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastComputedAt = LocalDateTime.now();
    }

    // Constructors
    public UserLayoutPreferences() {}

    public UserLayoutPreferences(String userId, String computedLayout) {
        this.userId = userId;
        this.computedLayout = computedLayout;
    }

    // Getters and Setters
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

    public String getComputedLayout() {
        return computedLayout;
    }

    public void setComputedLayout(String computedLayout) {
        this.computedLayout = computedLayout;
    }

    public String getMarketTheme() {
        return marketTheme;
    }

    public void setMarketTheme(String marketTheme) {
        this.marketTheme = marketTheme;
    }

    public String getEffectivePermissions() {
        return effectivePermissions;
    }

    public void setEffectivePermissions(String effectivePermissions) {
        this.effectivePermissions = effectivePermissions;
    }

    public String getPrimaryMarket() {
        return primaryMarket;
    }

    public void setPrimaryMarket(String primaryMarket) {
        this.primaryMarket = primaryMarket;
    }

    public String[] getBaseRoles() {
        return baseRoles;
    }

    public void setBaseRoles(String[] baseRoles) {
        this.baseRoles = baseRoles;
    }

    public LocalDateTime getCacheExpiry() {
        return cacheExpiry;
    }

    public void setCacheExpiry(LocalDateTime cacheExpiry) {
        this.cacheExpiry = cacheExpiry;
    }

    public LocalDateTime getLastComputedAt() {
        return lastComputedAt;
    }

    public void setLastComputedAt(LocalDateTime lastComputedAt) {
        this.lastComputedAt = lastComputedAt;
    }

    public String getComputationSource() {
        return computationSource;
    }

    public void setComputationSource(String computationSource) {
        this.computationSource = computationSource;
    }

    @Override
    public String toString() {
        return "UserLayoutPreferences{" +
                "userId='" + userId + '\'' +
                ", primaryMarket='" + primaryMarket + '\'' +
                ", cacheExpiry=" + cacheExpiry +
                ", lastComputedAt=" + lastComputedAt +
                ", computationSource='" + computationSource + '\'' +
                '}';
    }
}
