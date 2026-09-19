package com.ibrahim.study_platform.membership;

import org.springframework.data.jpa.repository.JpaRepository;

// Provides database operations for Membership entities.
public interface MembershipRepository extends JpaRepository<Membership, Long>{

    // Checks whether a Project has at least one Membership.
    boolean existsByProjectId(Long projectId);

    // Checks whether a specific User is already a member of a specific Project.
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

}
