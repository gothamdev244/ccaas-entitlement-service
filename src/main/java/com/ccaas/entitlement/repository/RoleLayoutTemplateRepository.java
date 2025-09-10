package com.ccaas.entitlement.repository;

import com.ccaas.entitlement.entity.RoleLayoutTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for RoleLayoutTemplate entity operations.
 * 
 * Provides data access methods for Layer 1 of the entitlement system.
 */
@Repository
public interface RoleLayoutTemplateRepository extends JpaRepository<RoleLayoutTemplate, java.util.UUID> {

    /**
     * Find all active role templates.
     */
    List<RoleLayoutTemplate> findByIsActiveTrue();

    /**
     * Find role template by name (case-insensitive).
     */
    @Query("SELECT rlt FROM RoleLayoutTemplate rlt WHERE LOWER(rlt.roleName) = LOWER(:roleName) AND rlt.isActive = true")
    Optional<RoleLayoutTemplate> findByRoleNameIgnoreCaseAndActive(@Param("roleName") String roleName);

    /**
     * Find role template by name.
     */
    Optional<RoleLayoutTemplate> findByRoleNameAndIsActiveTrue(String roleName);

    /**
     * Count active role templates.
     */
    long countByIsActiveTrue();
}
