package com.ibrahim.study_platform.task;

import com.ibrahim.study_platform.task.dto.CreateTaskRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.ibrahim.study_platform.task.dto.TaskUpdateRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

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

    @GetMapping("/tasks")
    public List<Task> getAllTasks(){
        return taskService.getAllTasks();
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id){
        Optional<Task> task = taskService.getTaskById(id);

        if(task.isPresent()){
            return ResponseEntity.ok(task.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskUpdateRequest request){
        Task task = taskService.updateTask(id, request);

        if(task != null){
            return ResponseEntity.ok(task);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        boolean deleted = taskService.deleteTask(id);
        if(deleted){
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.noContent().build();
    }
}