package com.ccaas.entitlement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CCAAS Entitlement Computation Service")
                        .version("1.0.0")
                        .description("""
                            **CCAAS Platform - Entitlement Computation Service (Data Access Layer)**
                            
                            This service provides the data access layer for the CCAAS entitlement system.
                            It handles database operations, caching, and internal layout computation for the PAPI service.
                            
                            ## Key Features:
                            - **Database Access**: PostgreSQL with HikariCP connection pooling
                            - **Role Templates**: Base role configurations and permissions
                            - **AD Group Overrides**: Market-specific layout overrides
                            - **User Preferences**: Cached user layout preferences
                            - **Internal Layout Computation**: Core layout computation logic
                            - **Performance Caching**: In-memory caching with Caffeine
                            - **Audit Trail**: Performance metrics and computation tracking
                            
                            ## Architecture Role:
                            ```
                            PAPI Service (Port 8094)
                                ↓ (HTTP calls)
                            SAPI Service (Port 8093) ← This Service
                                ↓ (Database queries)
                            PostgreSQL Database
                            ```
                            
                            ## Service Details:
                            - **Port**: 8093
                            - **Context Path**: /entitlement
                            - **Technology**: Spring Boot 3.x + Java 21
                            - **Database**: PostgreSQL with HikariCP
                            - **Caching**: Caffeine in-memory cache
                            
                            ## Internal APIs:
                            - **Layout Computation**: `/api/v1/layout/compute-internal` (called by PAPI)
                            - **Data Management**: CRUD operations for templates, overrides, preferences
                            - **Health Monitoring**: Service health and performance metrics
                            
                            ## Performance:
                            - **Response Time**: < 100ms for cached layouts
                            - **Throughput**: 1000+ requests/second
                            - **Cache Hit Rate**: 95%+ for repeated requests
                            - **Database**: Optimized queries with proper indexing
                            """)
                        .contact(new Contact()
                                .name("CCAAS Platform Team")
                                .email("platform@ccaas.com")
                                .url("https://github.com/gothamdev244/ccaas-platform"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8093/entitlement")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.ccaas.com/entitlement")
                                .description("Production Server")
                ));
    }
}
