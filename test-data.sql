-- Test data for CCAAS Entitlement Computation Service
-- This will populate the database with sample data for testing

-- Insert Role Layout Template for Senior Manager
INSERT INTO role_layout_templates (
    id, role_name, role_display_name, role_description,
    default_columns, available_widgets, default_actions, settings_access, default_theme,
    layout_priority, market_applicable, environment_types, is_active, created_at, updated_at
) VALUES (
    gen_random_uuid(),
    'SENIOR_MANAGER',
    'Senior Manager',
    'Layout template for senior management roles',
    '{"columns": [{"id": "dashboard", "width": 6}, {"id": "reports", "width": 6}]}',
    '{"widgets": ["call-summary", "agent-performance", "queue-status", "reports"]}',
    '{"actions": ["view-reports", "manage-agents", "configure-queues"]}',
    '{"access": ["admin-settings", "user-management", "system-config"]}',
    '{"theme": "corporate-blue", "logo": "hsbc-logo"}',
    100,
    ARRAY['EMEA', 'UK', 'US', 'APAC'],
    ARRAY['PRODUCTION', 'STAGING'],
    true,
    NOW(),
    NOW()
) ON CONFLICT (role_name) DO NOTHING;

-- Insert AD Group Override for EMEA Senior Managers
INSERT INTO ad_group_layout_overrides (
    ad_group_hash, ad_group_dn, parsed_market, parsed_function, parsed_environment,
    layout_overrides, data_restrictions, visual_customizations, priority, is_active, created_at, updated_at
) VALUES (
    'sha256_emea_senior_managers',
    'CN=EMEA-Senior-Managers,OU=Groups,DC=hsbc,DC=com',
    'EMEA',
    'MANAGEMENT',
    'PRODUCTION',
    '{"overrides": {"theme": "emea-corporate", "compliance": "gdpr-enabled"}}',
    '{"restrictions": {"data-retention": "90-days", "audit-logging": "required"}}',
    '{"customizations": {"logo-variant": "emea-logo", "color-scheme": "blue-green"}}',
    50,
    true,
    NOW(),
    NOW()
) ON CONFLICT (ad_group_hash) DO NOTHING;

-- Insert User Layout Preferences for test user
INSERT INTO user_layout_preferences (
    user_id, user_email, computed_layout, market_theme, effective_permissions,
    primary_market, base_roles, cache_expiry, last_computed_at, computation_source
) VALUES (
    '12345',
    'john.doe@hsbc.com',
    '{"layout": {"dashboard": {"widgets": ["call-summary", "agent-performance"]}, "sidebar": {"collapsed": false}}}',
    '{"theme": "emea-corporate", "colors": {"primary": "#1e3a8a", "secondary": "#059669"}}',
    '{"permissions": ["view-reports", "manage-agents", "configure-queues", "admin-settings"]}',
    'EMEA',
    ARRAY['SENIOR_MANAGER'],
    NOW() + INTERVAL '4 hours',
    NOW(),
    'computation'
) ON CONFLICT (user_id) DO NOTHING;