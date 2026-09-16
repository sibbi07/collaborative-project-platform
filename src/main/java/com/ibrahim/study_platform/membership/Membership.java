package com.ibrahim.study_platform.membership;

import com.ibrahim.study_platform.user.User;
import com.ibrahim.study_platform.project.Project;

import jakarta.persistence.Entity;
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
    private String role;
    private LocalDateTime joinedAt;

    protected Membership(){}


    @ManyToOne 
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne 
    @JoinColumn(name = "project_id")
    private Project project;
}
