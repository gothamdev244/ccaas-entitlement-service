package com.ccaas.entitlement.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * Entity representing AD group layout overrides (Layer 2 of entitlement system).
 * 
 * Stores market-specific overrides, compliance restrictions, and visual customizations
 * based on Active Directory group Distinguished Names (DNs).
 */
@Entity
@Table(name = "ad_group_layout_overrides")
public class AdGroupLayoutOverride {

    @Id
    @Column(name = "ad_group_hash", length = 64)
    private String adGroupHash;

    @Column(name = "ad_group_dn", length = 500, nullable = false)
    private String adGroupDn;

    @Column(name = "parsed_market", length = 10, nullable = false)
    private String parsedMarket;

    @Column(name = "parsed_function", length = 50)
    private String parsedFunction;

    @Column(name = "parsed_environment", length = 20)
    private String parsedEnvironment;

    @Column(name = "layout_overrides", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String layoutOverrides;

    @Column(name = "data_restrictions", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String dataRestrictions;

    @Column(name = "visual_customizations", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String visualCustomizations;

    @Column(name = "priority", nullable = false)
    private Integer priority = 100;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public AdGroupLayoutOverride() {}

    public AdGroupLayoutOverride(String adGroupHash, String adGroupDn, String parsedMarket) {
        this.adGroupHash = adGroupHash;
        this.adGroupDn = adGroupDn;
        this.parsedMarket = parsedMarket;
    }

    // Getters and Setters
    public String getAdGroupHash() {
        return adGroupHash;
    }

    public void setAdGroupHash(String adGroupHash) {
        this.adGroupHash = adGroupHash;
    }

    public String getAdGroupDn() {
        return adGroupDn;
    }

    public void setAdGroupDn(String adGroupDn) {
        this.adGroupDn = adGroupDn;
    }

    public String getParsedMarket() {
        return parsedMarket;
    }

    public void setParsedMarket(String parsedMarket) {
        this.parsedMarket = parsedMarket;
    }

    public String getParsedFunction() {
        return parsedFunction;
    }

    public void setParsedFunction(String parsedFunction) {
        this.parsedFunction = parsedFunction;
    }

    public String getParsedEnvironment() {
        return parsedEnvironment;
    }

    public void setParsedEnvironment(String parsedEnvironment) {
        this.parsedEnvironment = parsedEnvironment;
    }

    public String getLayoutOverrides() {
        return layoutOverrides;
    }

    public void setLayoutOverrides(String layoutOverrides) {
        this.layoutOverrides = layoutOverrides;
    }

    public String getDataRestrictions() {
        return dataRestrictions;
    }

    public void setDataRestrictions(String dataRestrictions) {
        this.dataRestrictions = dataRestrictions;
    }

    public String getVisualCustomizations() {
        return visualCustomizations;
    }

    public void setVisualCustomizations(String visualCustomizations) {
        this.visualCustomizations = visualCustomizations;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "AdGroupLayoutOverride{" +
                "adGroupHash='" + adGroupHash + '\'' +
                ", adGroupDn='" + adGroupDn + '\'' +
                ", parsedMarket='" + parsedMarket + '\'' +
                ", priority=" + priority +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}
