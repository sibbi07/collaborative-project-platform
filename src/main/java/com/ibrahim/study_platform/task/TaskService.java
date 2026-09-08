package com.ibrahim.study_platform.task;

import org.springframework.stereotype.Service;

@Service 
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public Task createTask(String title){
        Task task = new Task();
        task.setTitle(title);
        
        return taskRepository.save(task);
    }
    
}
