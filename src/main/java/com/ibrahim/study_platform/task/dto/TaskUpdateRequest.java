package com.ibrahim.study_platform.task.dto;
import com.ibrahim.study_platform.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskUpdateRequest {
    @NotBlank 
    @Size(max = 100)
    private String title;
    private TaskStatus status;

    
    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public TaskStatus getStatus(){
        return status;
    }

    public void setCompleted(TaskStatus status){
        this.status = status;
    }

}
