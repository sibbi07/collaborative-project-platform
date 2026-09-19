package com.ibrahim.study_platform.task;

import com.ibrahim.study_platform.project.Project;
import com.ibrahim.study_platform.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Task {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank 
    @Size(max = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TODO;

    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and setters provide access to the Task's editable properties.
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    // Automatically record when the Task is first saved to the database.
    @PrePersist 
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    // Automatically record when an existing Task is updated in the database.
    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    // Each Task belongs to exactly one Project.
    @ManyToOne 
    @JoinColumn(name="project_id", nullable=false)
    private Project project;

    // A Task can optionally be assigned to a User.
    @ManyToOne 
    @JoinColumn(name="assignee_id")
    private User assignee;
}