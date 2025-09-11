package com.ccaas.permission.config;

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
                        .title("CCAAS Entitlement Orchestration Service")
                        .version("1.0.0")
                        .description("""
                            **CCAAS Platform - Entitlement Orchestration Service (Business Logic Layer)**
                            
                            This service acts as the business logic orchestrator for the CCAAS entitlement system.
                            It handles OAuth authentication with AD group extraction and instant layout delivery.
                            
                            ## Key Features:
                            - **Header-Based Authentication**: Accepts amtoken and ad-groups from UI headers
                            - **HSBC Token Validation**: Integrates with HSBC internal API (placeholder implementation)
                            - **AD Group Processing**: Parses AD group DNs to extract market, function, environment
                            - **Layout Orchestration**: Coordinates with SAPI service for layout computation
                            - **Market Theming**: Applies market-specific themes (EMEA=Blue, UK=Red, US=Green, APAC=Purple)
                            
                            ## Architecture Flow:
                            ```
                            UI (Headers: amtoken, ad-groups)
                                ↓
                            PAPI Service (Port 8094)
                                ↓
                            HSBC Internal API (Token Validation → employeeId)
                                ↓
                            SAPI Service (Port 8093) → Layout Computation
                                ↓
                            UI ← Complete Layout Response
                            ```
                            
                            ## Service Details:
                            - **Port**: 8094
                            - **Context Path**: /permission
                            - **Technology**: Spring Boot 3.x + Java 21
                            - **Integration**: Calls SAPI service for data access
                            
                            ## Authentication:
                            - **Method**: Header-based authentication
                            - **Headers**: 
                              - `amtoken`: JWT token for user authentication
                              - `ad-groups`: Comma-separated AD group DNs
                            
                            ## Response Format:
                            - **Layout Configuration**: User-specific UI layout
                            - **Market Theme**: Visual theming based on AD groups
                            - **Permissions**: Computed access rights
                            - **Performance Metrics**: Computation timing
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
                                .url("http://localhost:8094/permission")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.ccaas.com/permission")
                                .description("Production Server")
                ));
    }
}
