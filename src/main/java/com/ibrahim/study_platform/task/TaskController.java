package com.ibrahim.study_platform.task;

import com.ibrahim.study_platform.task.dto.CreateTaskRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibrahim.study_platform.task.dto.CreateTaskRequest;

@RestController 
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    public Task addTask(@RequestBody CreateTaskRequest request){
        return taskService.createTask(request.getTitle());
    }
}