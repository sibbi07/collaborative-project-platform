package com.ibrahim.study_platform.task.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class CreateTaskRequest {
    @NotBlank
    @Size(max = 100)
    private String title;

    public CreateTaskRequest(){}

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
}
