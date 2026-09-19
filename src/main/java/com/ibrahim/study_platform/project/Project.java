package com.ibrahim.study_platform.project;

import com.ibrahim.study_platform.membership.Membership;
import com.ibrahim.study_platform.task.Task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
public class Project {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    

    // Store all Memberships belonging to this Project.
    // The Membership entity owns the relationship through its "project" field.
    @OneToMany(mappedBy = "project")
    private List<Membership> memberships = new ArrayList<Membership>();

    // Store all Tasks belonging to this Project.
    // The Task entity owns the relationship through its "project" field.
    @OneToMany(mappedBy="project")
    private List<Task> tasks = new ArrayList<Task>();

    // Protected no-argument constructor required by JPA when creating Project entities.
    protected Project(){}

    //Getters and setters
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public LocalDateTime getDeadline(){
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline){
        this.deadline = deadline;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt){
        this.updatedAt = updatedAt;
    }

    public List<Membership> getMemberships(){
        return memberships;
    }

    public void setMemberships(List<Membership> memberships){
        this.memberships = memberships;
    }

    public List<Task> getTasks(){
        return tasks;
    }

    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
    }
}