package com.ibrahim.study_platform.task.dto;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class CreateTaskRequest {
    @NotBlank
    @Size(max = 100)
    private String title;
    private LocalDateTime deadline;
    private Long assigneeId;
    private Long projectId;

    public CreateTaskRequest(){}

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public LocalDateTime getDeadline(){
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline){
        this.deadline = deadline;
    }

    public Long getProjectId(){
        return projectId;
    }

    public void setProjectId(Long projectId){
        this.projectId = projectId;
    }

    public Long getAssigneeId(){
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId){
        this.assigneeId = assigneeId;
    }
}
