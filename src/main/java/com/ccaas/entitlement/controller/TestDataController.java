package com.ccaas.entitlement.controller;

import com.ccaas.entitlement.entity.AdGroupLayoutOverride;
import com.ccaas.entitlement.entity.RoleLayoutTemplate;
import com.ccaas.entitlement.entity.UserLayoutPreferences;
import com.ccaas.entitlement.repository.AdGroupOverrideRepository;
import com.ccaas.entitlement.repository.RoleLayoutTemplateRepository;
import com.ccaas.entitlement.repository.UserPreferenceCacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/test")
public class TestDataController {

    private static final Logger logger = LoggerFactory.getLogger(TestDataController.class);

    @Autowired
    private RoleLayoutTemplateRepository roleTemplateRepository;

    @Autowired
    private AdGroupOverrideRepository adGroupOverrideRepository;

    @Autowired
    private UserPreferenceCacheRepository userPreferenceRepository;

    @PostMapping("/insert-test-data")
    public ResponseEntity<Map<String, Object>> insertTestData() {
        logger.info("Inserting test data into database");

        try {
            // Insert Role Layout Template
            RoleLayoutTemplate roleTemplate = new RoleLayoutTemplate();
            roleTemplate.setId(UUID.randomUUID());
            roleTemplate.setRoleName("SENIOR_MANAGER");
            roleTemplate.setRoleDisplayName("Senior Manager");
            roleTemplate.setRoleDescription("Layout template for senior management roles");
            roleTemplate.setDefaultColumns("{\"columns\": [{\"id\": \"dashboard\", \"width\": 6}, {\"id\": \"reports\", \"width\": 6}]}");
            roleTemplate.setAvailableWidgets("{\"widgets\": [\"call-summary\", \"agent-performance\", \"queue-status\", \"reports\"]}");
            roleTemplate.setDefaultActions("{\"actions\": [\"view-reports\", \"manage-agents\", \"configure-queues\"]}");
            roleTemplate.setSettingsAccess("{\"access\": [\"admin-settings\", \"user-management\", \"system-config\"]}");
            roleTemplate.setDefaultTheme("{\"theme\": \"corporate-blue\", \"logo\": \"hsbc-logo\"}");
            roleTemplate.setLayoutPriority(100);
            roleTemplate.setMarketApplicable(Arrays.asList("EMEA", "UK", "US", "APAC").toArray(new String[0]));
            roleTemplate.setEnvironmentTypes(Arrays.asList("PRODUCTION", "STAGING").toArray(new String[0]));
            roleTemplate.setIsActive(true);
            roleTemplate.setCreatedAt(LocalDateTime.now());
            roleTemplate.setUpdatedAt(LocalDateTime.now());

            roleTemplateRepository.save(roleTemplate);
            logger.info("Inserted role template: SENIOR_MANAGER");

            // Insert AD Group Override
            AdGroupLayoutOverride adGroupOverride = new AdGroupLayoutOverride();
            adGroupOverride.setAdGroupHash("sha256_emea_senior_managers");
            adGroupOverride.setAdGroupDn("CN=EMEA-Senior-Managers,OU=Groups,DC=hsbc,DC=com");
            adGroupOverride.setParsedMarket("EMEA");
            adGroupOverride.setParsedFunction("MANAGEMENT");
            adGroupOverride.setParsedEnvironment("PRODUCTION");
            adGroupOverride.setLayoutOverrides("{\"overrides\": {\"theme\": \"emea-corporate\", \"compliance\": \"gdpr-enabled\"}}");
            adGroupOverride.setDataRestrictions("{\"restrictions\": {\"data-retention\": \"90-days\", \"audit-logging\": \"required\"}}");
            adGroupOverride.setVisualCustomizations("{\"customizations\": {\"logo-variant\": \"emea-logo\", \"color-scheme\": \"blue-green\"}}");
            adGroupOverride.setPriority(50);
            adGroupOverride.setIsActive(true);
            adGroupOverride.setCreatedAt(LocalDateTime.now());
            adGroupOverride.setUpdatedAt(LocalDateTime.now());

            adGroupOverrideRepository.save(adGroupOverride);
            logger.info("Inserted AD group override for EMEA Senior Managers");

            // Insert User Layout Preferences
            UserLayoutPreferences userPreferences = new UserLayoutPreferences();
            userPreferences.setUserId("12345");
            userPreferences.setUserEmail("john.doe@hsbc.com");
            userPreferences.setComputedLayout("{\"layout\": {\"dashboard\": {\"widgets\": [\"call-summary\", \"agent-performance\"]}, \"sidebar\": {\"collapsed\": false}}}");
            userPreferences.setMarketTheme("{\"theme\": \"emea-corporate\", \"colors\": {\"primary\": \"#1e3a8a\", \"secondary\": \"#059669\"}}");
            userPreferences.setEffectivePermissions("{\"permissions\": [\"view-reports\", \"manage-agents\", \"configure-queues\", \"admin-settings\"]}");
            userPreferences.setPrimaryMarket("EMEA");
            userPreferences.setBaseRoles(Arrays.asList("SENIOR_MANAGER").toArray(new String[0]));
            userPreferences.setCacheExpiry(LocalDateTime.now().plusHours(4));
            userPreferences.setLastComputedAt(LocalDateTime.now());
            userPreferences.setComputationSource("computation");

            userPreferenceRepository.save(userPreferences);
            logger.info("Inserted user preferences for user: 12345");

            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Test data inserted successfully",
                "inserted", Map.of(
                    "roleTemplate", "SENIOR_MANAGER",
                    "adGroupOverride", "EMEA-Senior-Managers",
                    "userPreferences", "12345"
                )
            ));

        } catch (Exception e) {
            logger.error("Failed to insert test data: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error",
                "message", "Failed to insert test data: " + e.getMessage()
            ));
        }
    }
}
