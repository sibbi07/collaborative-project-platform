package com.ibrahim.study_platform.task;

import com.ibrahim.study_platform.task.dto.TaskUpdateRequest;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service 
public class TaskService {

    private final TaskRepository taskRepository;

    // Inject the repository required to store and retrieve Tasks from the database.
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    // Create a new Task, set its title, and save it to the database.
    public Task createTask(String title){
        Task task = new Task();
        task.setTitle(title);
        
        return taskRepository.save(task);
    }

    // Retrieve all Tasks from the database.
    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    // Attempt to retrieve a Task by its ID. Optional is used because the Task may not exist.
    public Optional<Task> getTaskById(Long id){
        return taskRepository.findById(id);
    }

    // Update an existing Task with the values provided in the request.
    public Task updateTask(Long id, TaskUpdateRequest request) {
        Optional<Task> task = getTaskById(id);

        // Return null so the controller can respond with 404 if the Task does not exist.
        if (task.isEmpty()) {
            return null;
        }

        Task existingTask = task.get();

        // Update the Task's editable properties with the values from the request.
        existingTask.setTitle(request.getTitle());
        existingTask.setStatus(request.getStatus());

        // Save the updated Task and return the persisted entity.
        return taskRepository.save(existingTask);
    }

    // Delete a Task by its ID and indicate whether the Task existed.
    public boolean deleteTask(Long id){
        Optional<Task> task = getTaskById(id);

        // Return false so the controller knows that no Task was found to delete.
        if(task.isEmpty()){
            return false;
        }

        taskRepository.deleteById(id);

        // Confirm that the Task existed and was successfully deleted.
        return true;
    }
    
}