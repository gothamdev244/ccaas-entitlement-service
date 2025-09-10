package com.ccaas.entitlement.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Entity representing role layout templates (Layer 1 of entitlement system).
 * 
 * Stores base UI layouts, widgets, actions, and settings for each role.
 * These templates serve as the foundation for user-specific layouts.
 */
@Entity
@Table(name = "role_layout_templates")
public class RoleLayoutTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private java.util.UUID id;

    @Column(name = "role_name", length = 100, nullable = false, unique = true)
    private String roleName;

    @Column(name = "role_display_name", length = 255, nullable = false)
    private String roleDisplayName;

    @Column(name = "role_description", columnDefinition = "text")
    private String roleDescription;

    @Column(name = "default_columns", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String defaultColumns;

    @Column(name = "available_widgets", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String availableWidgets;

    @Column(name = "default_actions", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String defaultActions;

    @Column(name = "settings_access", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String settingsAccess;

    @Column(name = "default_theme", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String defaultTheme;

    @Column(name = "layout_priority")
    private Integer layoutPriority = 0;

    @Column(name = "market_applicable", columnDefinition = "text[]")
    private String[] marketApplicable;

    @Column(name = "environment_types", columnDefinition = "text[]")
    private String[] environmentTypes;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
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
    public RoleLayoutTemplate() {}

    public RoleLayoutTemplate(String roleName, String roleDisplayName, String defaultColumns) {
        this.roleName = roleName;
        this.roleDisplayName = roleDisplayName;
        this.defaultColumns = defaultColumns;
    }

    // Getters and Setters
    public java.util.UUID getId() {
        return id;
    }

    public void setId(java.util.UUID id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDisplayName() {
        return roleDisplayName;
    }

    public void setRoleDisplayName(String roleDisplayName) {
        this.roleDisplayName = roleDisplayName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public String getDefaultColumns() {
        return defaultColumns;
    }

    public void setDefaultColumns(String defaultColumns) {
        this.defaultColumns = defaultColumns;
    }

    public String getAvailableWidgets() {
        return availableWidgets;
    }

    public void setAvailableWidgets(String availableWidgets) {
        this.availableWidgets = availableWidgets;
    }

    public String getDefaultActions() {
        return defaultActions;
    }

    public void setDefaultActions(String defaultActions) {
        this.defaultActions = defaultActions;
    }

    public String getSettingsAccess() {
        return settingsAccess;
    }

    public void setSettingsAccess(String settingsAccess) {
        this.settingsAccess = settingsAccess;
    }

    public String getDefaultTheme() {
        return defaultTheme;
    }

    public void setDefaultTheme(String defaultTheme) {
        this.defaultTheme = defaultTheme;
    }

    public Integer getLayoutPriority() {
        return layoutPriority;
    }

    public void setLayoutPriority(Integer layoutPriority) {
        this.layoutPriority = layoutPriority;
    }

    public String[] getMarketApplicable() {
        return marketApplicable;
    }

    public void setMarketApplicable(String[] marketApplicable) {
        this.marketApplicable = marketApplicable;
    }

    public String[] getEnvironmentTypes() {
        return environmentTypes;
    }

    public void setEnvironmentTypes(String[] environmentTypes) {
        this.environmentTypes = environmentTypes;
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
        return "RoleLayoutTemplate{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", roleDisplayName='" + roleDisplayName + '\'' +
                ", layoutPriority=" + layoutPriority +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}
