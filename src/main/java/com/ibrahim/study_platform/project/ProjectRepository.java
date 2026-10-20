package com.ibrahim.study_platform.project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findDistinctByMembershipsUserId(Long userId);

}