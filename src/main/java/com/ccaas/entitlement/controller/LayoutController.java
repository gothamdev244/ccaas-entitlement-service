package com.ccaas.entitlement.controller;

import com.ccaas.entitlement.dto.LayoutComputationRequest;
import com.ccaas.entitlement.dto.LayoutComputationResponse;
import com.ccaas.entitlement.service.LayoutComputationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/layout")
@Tag(name = "Internal Layout Computation", description = "Data access layer for layout computation - called by PAPI service")
public class LayoutController {

    private static final Logger logger = LoggerFactory.getLogger(LayoutController.class);

    @Autowired
    private LayoutComputationService layoutComputationService;

    @PostMapping(value = "/compute-internal",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Internal layout computation",
        description = """
            **Internal Layout Computation API**
            
            This endpoint is called internally by the PAPI service to compute user layouts.
            It performs the actual database queries and layout computation logic.
            
            **Process:**
            1. Receives userId and adGroups from PAPI service
            2. Queries role templates from database
            3. Applies AD group overrides based on market/function
            4. Computes final layout configuration
            5. Applies market theming (EMEA=Blue, UK=Red, US=Green, APAC=Purple)
            6. Returns complete layout with permissions and theme
            
            **Database Operations:**
            - Query role_layout_templates for base configurations
            - Query ad_group_layout_overrides for market-specific overrides
            - Query user_layout_preferences for cached preferences
            - Insert audit records for performance tracking
            
            **Performance:**
            - Response time: < 100ms
            - Database queries optimized with proper indexing
            - Caffeine cache for frequently accessed data
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
            description = "Invalid request parameters",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal server error or database connection issue",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<LayoutComputationResponse> computeLayoutInternal(
            @RequestBody LayoutComputationRequest request) {

        logger.info("Received internal layout computation request for user: {}", request.getUserId());

        try {
            LayoutComputationResponse response = layoutComputationService.computeLayout(request);
            logger.info("Internal layout computation completed successfully for user: {}", request.getUserId());
            return ResponseEntity.ok(response);
        } catch (Exception error) {
            logger.error("Internal layout computation failed: {}", error.getMessage());

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
    @Operation(summary = "Health check", description = "Returns SAPI service health status")
    @ApiResponse(responseCode = "200", description = "SAPI service is healthy")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("SAPI Service is running");
    }
}
