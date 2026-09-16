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

    @OneToMany(mappedBy = "project")
    private List<Membership> memberships = new ArrayList<Membership>();

    @OneToMany(mappedBy = "project")
    private List<Task> tasks = new ArrayList<Task>();

    protected Project(){}


    
}
