package com.ibrahim.study_platform.task.dto;

public class TaskUpdateRequest {
    private String title;
    private boolean completed;

    
    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public boolean getCompleted(){
        return completed;
    }

    public void setCompleted(boolean completed){
        this.completed = completed;
    }

}
