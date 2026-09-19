package com.ibrahim.study_platform.project.dto;

import java.time.LocalDateTime;

public class CreateProjectRequest {

    private String name;
    private String description;
    private LocalDateTime deadline;
    private Long creatorId;

    public CreateProjectRequest(){}

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

    public Long getCreatorId(){
        return creatorId;
    }

    public void setCreatorId(Long creatorId){
        this.creatorId = creatorId;
    }
}
