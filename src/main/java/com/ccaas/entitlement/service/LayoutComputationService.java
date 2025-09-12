package com.ccaas.entitlement.service;

import com.ccaas.entitlement.dto.LayoutComputationRequest;
import com.ccaas.entitlement.dto.LayoutComputationResponse;
import com.ccaas.entitlement.entity.RoleLayoutTemplate;
import com.ccaas.entitlement.entity.AdGroupLayoutOverride;
import com.ccaas.entitlement.entity.UserLayoutPreferences;
import com.ccaas.entitlement.repository.RoleLayoutTemplateRepository;
import com.ccaas.entitlement.repository.AdGroupOverrideRepository;
import com.ccaas.entitlement.repository.UserPreferenceCacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class LayoutComputationService {

    private static final Logger logger = LoggerFactory.getLogger(LayoutComputationService.class);

    @Autowired
    private RoleLayoutTemplateRepository roleTemplateRepository;

    @Autowired
    private AdGroupOverrideRepository adGroupOverrideRepository;

    @Autowired
    private UserPreferenceCacheRepository userPreferenceRepository;

    public LayoutComputationResponse computeLayout(LayoutComputationRequest request) {
        long startTime = System.currentTimeMillis();
        
        logger.info("Starting layout computation for user: {} with {} AD groups", 
                   request.getUserId(), request.getAdGroups().size());

        try {
            // Step 1: Get user preferences (if any)
            UserLayoutPreferences userPreferences = userPreferenceRepository
                .findByUserId(request.getUserId())
                .orElse(null);

            // Step 2: Check for AD group overrides
            List<AdGroupLayoutOverride> adGroupOverrides = new ArrayList<>();
            for (String adGroup : request.getAdGroups()) {
                AdGroupLayoutOverride override = adGroupOverrideRepository.findByAdGroupDnAndIsActiveTrue(adGroup);
                if (override != null) {
                    adGroupOverrides.add(override);
                }
            }

            // Step 3: Get role templates based on AD groups
            List<RoleLayoutTemplate> roleTemplates = new ArrayList<>();
            for (String adGroup : request.getAdGroups()) {
                // Extract role from AD group (simplified logic)
                String role = extractRoleFromAdGroup(adGroup);
                if (role != null) {
                    roleTemplateRepository.findByRoleNameAndIsActiveTrue(role)
                        .ifPresent(roleTemplates::add);
                }
            }

            // Step 4: Extract market from AD groups
            String market = extractMarketFromAdGroups(request.getAdGroups());

            // Step 5: Compute final layout
            Map<String, Object> computedLayout = computeFinalLayout(
                userPreferences, adGroupOverrides, roleTemplates);

            // Step 6: Build response
            LayoutComputationResponse response = new LayoutComputationResponse();
            response.setUserId(request.getUserId());
            response.setLayout(computedLayout);
            // UI handles market badge styling via useMarketIndicator hook
            response.setMarket(market);
            response.setComputationSource("sapi");
            response.setComputationTimeMs(System.currentTimeMillis() - startTime);
            response.setTimestamp(LocalDateTime.now());

            logger.info("Layout computation completed for user: {} in {}ms", 
                       request.getUserId(), response.getComputationTimeMs());

            return response;

        } catch (Exception e) {
            logger.error("Error computing layout for user: {}", request.getUserId(), e);
            throw new RuntimeException("Layout computation failed", e);
        }
    }

    private String extractRoleFromAdGroup(String adGroupDn) {
        // Simplified role extraction logic
        // TODO: Implement proper AD group parsing logic
        if (adGroupDn.contains("Senior-Managers")) {
            return "SENIOR_MANAGER";
        } else if (adGroupDn.contains("Managers")) {
            return "MANAGER";
        } else if (adGroupDn.contains("Analysts")) {
            return "ANALYST";
        }
        return null;
    }

    private Map<String, Object> computeFinalLayout(
            UserLayoutPreferences userPreferences,
            List<AdGroupLayoutOverride> adGroupOverrides,
            List<RoleLayoutTemplate> roleTemplates) {

        Map<String, Object> layout = new HashMap<>();

        // Start with role template defaults
        if (!roleTemplates.isEmpty()) {
            RoleLayoutTemplate primaryTemplate = roleTemplates.get(0);
            layout.put("defaultColumns", primaryTemplate.getDefaultColumns());
            layout.put("availableWidgets", primaryTemplate.getAvailableWidgets());
            layout.put("defaultActions", primaryTemplate.getDefaultActions());
            layout.put("settingsAccess", primaryTemplate.getSettingsAccess());
            layout.put("defaultTheme", primaryTemplate.getDefaultTheme());
        }

        // Apply AD group overrides
        for (AdGroupLayoutOverride override : adGroupOverrides) {
            if (override.getLayoutOverrides() != null) {
                layout.put("adGroupLayoutOverride", override.getLayoutOverrides());
            }
            if (override.getDataRestrictions() != null) {
                layout.put("dataRestrictions", override.getDataRestrictions());
            }
            if (override.getVisualCustomizations() != null) {
                layout.put("visualCustomizations", override.getVisualCustomizations());
            }
        }

        // Apply user preferences (highest priority)
        if (userPreferences != null) {
            if (userPreferences.getComputedLayout() != null) {
                layout.put("userComputedLayout", userPreferences.getComputedLayout());
            }
            if (userPreferences.getMarketTheme() != null) {
                layout.put("userMarketTheme", userPreferences.getMarketTheme());
            }
            if (userPreferences.getEffectivePermissions() != null) {
                layout.put("userEffectivePermissions", userPreferences.getEffectivePermissions());
            }
        }

        return layout;
    }

    /**
     * Extract market from AD groups
     */
    private String extractMarketFromAdGroups(List<String> adGroups) {
        for (String adGroup : adGroups) {
            if (adGroup.contains("EMEA")) {
                return "EMEA";
            } else if (adGroup.contains("UK")) {
                return "UK";
            } else if (adGroup.contains("US")) {
                return "US";
            } else if (adGroup.contains("APAC")) {
                return "APAC";
            }
        }
        return "GLOBAL";
    }


    /**
     * Build structured layout with columns, widgets, and permissions
     */
    private Map<String, Object> buildStructuredLayout(List<String> roles, String market) {
        Map<String, Object> layout = new HashMap<>();
        
        // Define column visibility based on roles
        Map<String, Object> columns = new HashMap<>();
        columns.put("customer", Map.of("visible", true, "size", 300));
        columns.put("transcript", Map.of("visible", true, "size", 400));
        
        // Embedded apps visibility depends on role
        boolean canViewEmbedded = roles.stream().anyMatch(role -> 
            role.contains("senior") || role.contains("supervisor") || role.contains("manager"));
        columns.put("embedded", Map.of("visible", canViewEmbedded, "size", 300));
        
        layout.put("columns", columns);
        
        // Define widget configurations
        Map<String, Object> widgets = new HashMap<>();
        widgets.put("sentiment_widget", Map.of("visible", true, "editable", false));
        widgets.put("priority_widget", Map.of("visible", roles.contains("supervisor"), "editable", false));
        layout.put("widgets", widgets);
        
        // Define data access permissions
        Map<String, Object> dataAccess = new HashMap<>();
        Map<String, Object> customerData = new HashMap<>();
        customerData.put("personalInfo", true);
        customerData.put("contactDetails", true);
        customerData.put("accountInfo", roles.stream().anyMatch(role -> 
            role.contains("senior") || role.contains("supervisor") || role.contains("manager")));
        
        dataAccess.put("customerData", customerData);
        dataAccess.put("interactionHistory", Map.of("full", 
            roles.stream().anyMatch(role -> role.contains("supervisor") || role.contains("manager"))));
        
        layout.put("dataAccess", dataAccess);
        
        // Define effective permissions
        Map<String, Object> permissions = new HashMap<>();
        permissions.put("data.customer.read", "granted");
        permissions.put("ui.customer_column.view", "granted");
        permissions.put("ui.transcript_column.view", "granted");
        permissions.put("ui.embedded_apps.view", canViewEmbedded ? "granted" : "denied");
        
        layout.put("effectivePermissions", permissions);
        
        return layout;
    }
}
