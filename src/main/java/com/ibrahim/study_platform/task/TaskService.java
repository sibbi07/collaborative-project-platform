package com.ibrahim.study_platform.task;
import com.ibrahim.study_platform.task.dto.TaskUpdateRequest;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id){
        return taskRepository.findById(id);
    }

    public Task updateTask(Long id, TaskUpdateRequest request) {
    Optional<Task> task = getTaskById(id);

        if (task.isEmpty()) {
            return null;
        }

        Task existingTask = task.get();

        existingTask.setTitle(request.getTitle());
        existingTask.setCompleted(request.getCompleted());

        return taskRepository.save(existingTask);
    }

    public boolean deleteTask(Long id){
        Optional<Task> task = getTaskById(id);
        if(task.isEmpty()){
            return false;
        }

        taskRepository.deleteById(id);
        return true;
    }
    
}
