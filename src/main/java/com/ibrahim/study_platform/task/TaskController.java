package com.ibrahim.study_platform.task;

import com.ibrahim.study_platform.task.dto.CreateTaskRequest;
import com.ibrahim.study_platform.task.dto.TaskResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.ibrahim.study_platform.task.dto.TaskUpdateRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import jakarta.validation.Valid;

@RequestMapping("/tasks")
@RestController 
public class TaskController {
    private final TaskService taskService;

    // Inject the TaskService responsible for handling task-related business logic.
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Create a new Task using the title provided in the request body.
    @PostMapping
    public TaskResponse addTask(@RequestBody @Valid CreateTaskRequest request){
        return taskService.createTask(
            request.getTitle(), 
            request.getProjectId(),
            request.getDeadline(),
            request.getAssigneeId());
    }

    // Retrieve all Tasks.
    @GetMapping
    public List<TaskResponse> getAllTasks(){
        return taskService.getAllTasks();
    }

    // Retrieve a Task by its ID and return 404 if the Task does not exist.
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id){
        Optional<TaskResponse> task = taskService.getTaskById(id);

        if(task.isPresent()){
            return ResponseEntity.ok(task.get());
        }

        return ResponseEntity.notFound().build();
    }

    // Update an existing Task and return 404 if the Task does not exist.
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody @Valid TaskUpdateRequest request){
        TaskResponse task = taskService.updateTask(id, request);

        if(task != null){
            return ResponseEntity.ok(task);
        }

        return ResponseEntity.notFound().build();
    }

    // Delete a Task by its ID and return an appropriate response based on whether it existed.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        boolean deleted = taskService.deleteTask(id);

        if(deleted){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}