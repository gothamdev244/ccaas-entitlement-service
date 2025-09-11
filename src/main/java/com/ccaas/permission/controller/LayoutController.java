package com.ccaas.permission.controller;

import com.ccaas.permission.dto.LayoutComputationRequest;
import com.ccaas.permission.dto.LayoutComputationResponse;
import com.ccaas.permission.service.PermissionService;
import com.ccaas.permission.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/layout")
@Tag(name = "Layout Computation", description = "OAuth Integration & AD Group Extraction for instant layout delivery")
public class LayoutController {
    
    private static final Logger logger = LoggerFactory.getLogger(LayoutController.class);
    
    private final PermissionService permissionService;
    private final OAuthService oauthService;
    
    @Autowired
    public LayoutController(PermissionService permissionService, OAuthService oauthService) {
        this.permissionService = permissionService;
        this.oauthService = oauthService;
    }
    
    @PostMapping(value = "/compute", 
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Compute user layout with OAuth integration",
        description = """
            **OAuth Integration & AD Group Extraction**
            
            Computes personalized user layout based on JWT token authentication and AD group membership.
            This endpoint implements the complete OAuth flow with instant layout delivery.
            
            **Flow:**
            1. UI sends amtoken (JWT) and ad-groups (comma-separated DNs) in headers
            2. PAPI validates token with HSBC internal API (placeholder)
            3. PAPI extracts employeeId from token
            4. PAPI calls SAPI service for layout computation
            5. SAPI computes layout based on user ID + AD groups + database
            6. PAPI returns complete layout with market theming
            
            **Headers Required:**
            - `amtoken`: JWT token for authentication
            - `ad-groups`: Comma-separated AD group DNs (e.g., "CN=EMEA-Senior-Managers,OU=Groups,DC=hsbc,DC=com")
            
            **Response Includes:**
            - Layout configuration (columns, widgets, permissions)
            - Market theme (EMEA=Blue, UK=Red, US=Green, APAC=Purple)
            - User permissions and access rights
            - Performance metrics (computation time)
            """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Layout computation successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LayoutComputationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request or authentication token",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal server error",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<LayoutComputationResponse> computeLayout(
            @Parameter(description = "AMTOKEN for authentication")
            @RequestHeader("amtoken") String amtoken,
            @Parameter(description = "Comma-separated AD groups")
            @RequestHeader("ad-groups") String adGroupsHeader) {
        
        logger.info("Received layout computation request with AMTOKEN: {}", 
                   amtoken.substring(0, Math.min(10, amtoken.length())) + "...");
        
        try {
            // Parse AD groups from header
            String[] adGroupsArray = adGroupsHeader.split(",");
            List<String> adGroups = Arrays.asList(adGroupsArray);
            logger.debug("Parsed {} AD groups from header", adGroups.size());
            
            // Create layout computation request
            LayoutComputationRequest request = new LayoutComputationRequest(amtoken, adGroups);
            
            // Compute layout
            LayoutComputationResponse response = permissionService.computeUserLayout(request);
            logger.info("Layout computation completed successfully for employee: {}", 
                       response.getEmployeeId());
            return ResponseEntity.ok(response);
        } catch (Exception error) {
            logger.error("Layout computation failed: {}", error.getMessage());
            
            LayoutComputationResponse errorResponse = new LayoutComputationResponse();
            errorResponse.setComputationSource("error");
            errorResponse.setComputationTimeMs(0L);
            
            if (error instanceof IllegalArgumentException) {
                return ResponseEntity.badRequest().body(errorResponse);
            } else {
                return ResponseEntity.internalServerError().body(errorResponse);
            }
        }
    }
    
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Returns service health status")
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("PAPI Service is running");
    }
}
