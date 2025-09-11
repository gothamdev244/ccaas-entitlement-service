package com.ccaas.entitlement.controller;

import com.ccaas.entitlement.dto.LayoutComputationRequest;
import com.ccaas.entitlement.dto.LayoutComputationResponse;
import com.ccaas.entitlement.service.LayoutComputationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/layout")
public class LayoutController {

    private static final Logger logger = LoggerFactory.getLogger(LayoutController.class);

    @Autowired
    private LayoutComputationService layoutComputationService;

    @PostMapping(value = "/compute-internal",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
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
}
