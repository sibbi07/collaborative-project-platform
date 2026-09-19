package com.ibrahim.study_platform.membership;

import com.ibrahim.study_platform.user.User;
import com.ibrahim.study_platform.project.Project;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Membership {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MembershipRole role;

    private LocalDateTime joinedAt;

    // Protected no-argument constructor required by JPA when creating Membership entities.
    protected Membership(){}

    // Each Membership is associated with one User and one Project.
    @ManyToOne 
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne 
    @JoinColumn(name="project_id", nullable=false)
    private Project project;

    // Getters and setters provide access to the Membership's stored properties.
    public Long getId(){
        return id;
    }

    public void setId(Long id){
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

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Project getProject(){
        return project;
    }

    public void setProject(Project project){
        this.project = project;
    }
}