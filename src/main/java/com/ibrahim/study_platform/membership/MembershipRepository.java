package com.ibrahim.study_platform.membership;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// Provides database operations for Membership entities.
public interface MembershipRepository extends JpaRepository<Membership, Long>{

    // Checks whether a Project has at least one Membership.
    boolean existsByProjectId(Long projectId);

    // Checks whether a specific User is already a member of a specific Project.
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    Optional<Membership> findByProjectIdAndUserId(Long projectId, Long userId);

    List<Membership> findByUserId(Long userId);

    void deleteByProjectId(Long projectId);

}
