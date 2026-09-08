package com.ibrahim.study_platform.task.dto;

public class CreateTaskRequest {
    private String title;

    public CreateTaskRequest(){}

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
}
