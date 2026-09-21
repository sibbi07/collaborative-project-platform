package com.ibrahim.study_platform.task;

import com.ibrahim.study_platform.project.Project;
import com.ibrahim.study_platform.project.ProjectRepository;
import com.ibrahim.study_platform.task.dto.TaskResponse;
import com.ibrahim.study_platform.task.dto.TaskUpdateRequest;
import com.ibrahim.study_platform.user.User;
import com.ibrahim.study_platform.user.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service 
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // Inject the repository required to store and retrieve Tasks from the database.
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository){
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // Create a new Task and save it to the database.
    public TaskResponse createTask(String title, Long projectId, LocalDateTime deadline, Long assigneeId){
        Task task = new Task();
        task.setTitle(title);
        task.setDeadline(deadline);
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + projectId + " does not exist."));
                
        if(assigneeId != null){
            User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + assigneeId + " does not exist."));
                
            task.setAssignee(assignee);
        }

        task.setProject(project);
        Task savedTask = taskRepository.save(task);
        
        return toResponse(savedTask);
    }

    // Retrieve all Tasks from the database.
    public List<TaskResponse> getAllTasks(){
        List<Task> taskList = taskRepository.findAll();
        return taskList.stream()
                .map(this::toResponse)
                .toList();
    }

    // Attempt to retrieve a Task by its ID. Optional is used because the Task may not exist.
    public Optional<TaskResponse> getTaskById(Long id){
        return taskRepository.findById(id)
                .map(this::toResponse);
    }

    // Update an existing Task with the values provided in the request.
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        Optional<Task> task = taskRepository.findById(id);

        // Return null so the controller can respond with 404 if the Task does not exist.
        if (task.isEmpty()) {
            return null;
        }

        Task existingTask = task.get();

        // Update the Task's editable properties with the values from the request.
        existingTask.setTitle(request.getTitle());
        existingTask.setStatus(request.getStatus());

        // Save the updated Task and return the persisted DTO.
        Task savedTask = taskRepository.save(existingTask);
        return toResponse(savedTask);
    }

    // Delete a Task by its ID and indicate whether the Task existed.
    public boolean deleteTask(Long id){
        Optional<Task> task = taskRepository.findById(id);

        // Return false so the controller knows that no Task was found to delete.
        if(task.isEmpty()){
            return false;
        }

        taskRepository.deleteById(id);

        // Confirm that the Task existed and was successfully deleted.
        return true;
    }

    private TaskResponse toResponse(Task task){
        TaskResponse response = new TaskResponse();
        
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setStatus(task.getStatus());
        response.setDeadline(task.getDeadline());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        response.setProjectId(task.getProject().getId());
        if(task.getAssignee() != null){
            response.setAssigneeId(task.getAssignee().getId());
        }

        return response;
    }
    
}