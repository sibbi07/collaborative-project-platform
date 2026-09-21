package com.ibrahim.study_platform.membership.dto;

import java.time.LocalDateTime;

import com.ibrahim.study_platform.membership.MembershipRole;

public class MembershipResponse {
    private Long id;
    private MembershipRole role;
    private LocalDateTime joinedAt;
    private Long userId;
    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MembershipRole getRole(){
        return role;
    }

    public void setRole(MembershipRole role){
        this.role = role;
    }

    public LocalDateTime getJoinedAt(){
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt){
        this.joinedAt = joinedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
