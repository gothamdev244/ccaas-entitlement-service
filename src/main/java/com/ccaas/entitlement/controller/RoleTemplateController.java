package com.ccaas.entitlement.controller;

import com.ccaas.entitlement.entity.RoleLayoutTemplate;
import com.ccaas.entitlement.repository.RoleLayoutTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Role Layout Template operations (Layer 1).
 * 
 * Provides endpoints for managing base role templates.
 */
@RestController
@RequestMapping("/api/v1/data/role-templates")
public class RoleTemplateController {

    @Autowired
    private RoleLayoutTemplateRepository repository;

    /**
     * Get all active role templates.
     */
    @GetMapping
    public ResponseEntity<List<RoleLayoutTemplate>> getAllRoleTemplates() {
        List<RoleLayoutTemplate> templates = repository.findByIsActiveTrue();
        return ResponseEntity.ok(templates);
    }

    /**
     * Get role template by name.
     */
    @GetMapping("/name/{roleName}")
    public ResponseEntity<RoleLayoutTemplate> getRoleTemplateByName(@PathVariable String roleName) {
        Optional<RoleLayoutTemplate> template = repository.findByRoleNameAndIsActiveTrue(roleName);
        
        if (template.isPresent()) {
            return ResponseEntity.ok(template.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create new role template.
     */
    @PostMapping
    public ResponseEntity<RoleLayoutTemplate> createRoleTemplate(@RequestBody RoleLayoutTemplate template) {
        template.setIsActive(true);
        
        RoleLayoutTemplate saved = repository.save(template);
        return ResponseEntity.ok(saved);
    }

    /**
     * Update role template by name.
     */
    @PutMapping("/name/{roleName}")
    public ResponseEntity<RoleLayoutTemplate> updateRoleTemplateByName(@PathVariable String roleName, 
                                                                     @RequestBody RoleLayoutTemplate template) {
        Optional<RoleLayoutTemplate> existing = repository.findByRoleNameAndIsActiveTrue(roleName);
        
        if (existing.isPresent()) {
            template.setId(existing.get().getId());
            template.setRoleName(roleName);
            template.setIsActive(true);
            RoleLayoutTemplate saved = repository.save(template);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete role template by name (soft delete).
     */
    @DeleteMapping("/name/{roleName}")
    public ResponseEntity<Void> deleteRoleTemplateByName(@PathVariable String roleName) {
        Optional<RoleLayoutTemplate> template = repository.findByRoleNameAndIsActiveTrue(roleName);
        
        if (template.isPresent()) {
            template.get().setIsActive(false);
            repository.save(template.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get role template count.
     */
    @GetMapping("/count")
    public ResponseEntity<Object> getRoleTemplateCount() {
        long count = repository.countByIsActiveTrue();
        return ResponseEntity.ok(new Object() {
            public final long activeTemplates = count;
        });
    }

    /**
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<Object> health() {
        return ResponseEntity.ok(new Object() {
            public final String status = "UP";
            public final String service = "RoleTemplateController";
            public final long activeTemplates = repository.countByIsActiveTrue();
        });
    }
}
