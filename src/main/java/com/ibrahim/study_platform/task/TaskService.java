package com.ibrahim.study_platform.task;

import com.ibrahim.study_platform.project.Project;
import com.ibrahim.study_platform.project.ProjectRepository;
import com.ibrahim.study_platform.task.dto.TaskResponse;
import com.ibrahim.study_platform.task.dto.TaskUpdateRequest;
import com.ibrahim.study_platform.user.User;
import com.ibrahim.study_platform.user.UserRepository;
import com.ibrahim.study_platform.membership.MembershipRepository;
import com.ibrahim.study_platform.membership.Membership;
import com.ibrahim.study_platform.membership.MembershipRole;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service 
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    // Inject the repository required to store and retrieve Tasks from the database.
    public TaskService(
            TaskRepository taskRepository, 
            ProjectRepository projectRepository, 
            UserRepository userRepository,
            MembershipRepository membershipRepository){
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
    }

    // Create a new Task and save it to the database.
    public TaskResponse createTask(
            String title,
            Long projectId,
            LocalDateTime deadline,
            Long assigneeId,
            Long requestingUserId) {

        // Find the project.
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Project with ID " + projectId + " does not exist."));

        // Check that the requesting user belongs to this project.
        Membership requesterMembership = membershipRepository
                .findByProjectIdAndUserId(projectId, requestingUserId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "You must be a member of this project to create tasks."));

        // Only OWNERs and ADMINs can create tasks.
        MembershipRole requesterRole = requesterMembership.getRole();

        if (requesterRole != MembershipRole.OWNER
                && requesterRole != MembershipRole.ADMIN) {
            throw new IllegalArgumentException(
                    "Only project owners and admins can create tasks.");
        }

        // Create the task.
        Task task = new Task();
        task.setTitle(title);
        task.setDeadline(deadline);
        task.setProject(project);

        // If an assignee was supplied, validate them.
        if (assigneeId != null) {

            // The user must exist.
            User assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "User with ID " + assigneeId + " does not exist."));

            // The assignee must also belong to this same project.
            membershipRepository.findByProjectIdAndUserId(projectId, assigneeId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "The assignee must be a member of this project."));

            task.setAssignee(assignee);
        }

        // Save the task and return its response DTO.
        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    public List<TaskResponse> getAllTasks(Long requestingUserId) {

        // Find all project memberships belonging to the requesting user.
        List<Membership> memberships =
                membershipRepository.findByUserId(requestingUserId);

        // Collect tasks from the projects the user belongs to.
        List<Task> tasks = new ArrayList<>();

        for (Membership membership : memberships) {
            Long projectId = membership.getProject().getId();

            tasks.addAll(taskRepository.findByProjectId(projectId));
        }

        // Convert the tasks into response DTOs.
        return tasks.stream()
                .map(this::toResponse)
                .toList();
    }

    // Attempt to retrieve a Task by its ID. Optional is used because the Task may not exist.
    public Optional<TaskResponse> getTaskById(Long id, Long requestingUserId) {

        // Find the task. If it doesn't exist, return an empty Optional.
        Optional<Task> task = taskRepository.findById(id);

        if (task.isEmpty()) {
            return Optional.empty();
        }

        Task existingTask = task.get();

        // Check whether the requester belongs to the task's project.
        membershipRepository
                .findByProjectIdAndUserId(
                        existingTask.getProject().getId(),
                        requestingUserId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "You must be a member of this project to view its tasks."));

        // Return the task only after authorisation succeeds.
        return Optional.of(toResponse(existingTask));
    }

    // Update an existing Task with the values provided in the request.
    public TaskResponse updateTask(
            Long id,
            TaskUpdateRequest request,
            Long requestingUserId) {

        // Find the task.
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Task with ID " + id + " does not exist."));

        // Check the requester's membership in the task's project.
        Membership requesterMembership = membershipRepository
                .findByProjectIdAndUserId(
                        existingTask.getProject().getId(),
                        requestingUserId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "You must be a member of this project to update its tasks."));

        // Only OWNERs and ADMINs can update tasks.
        MembershipRole role = requesterMembership.getRole();

        if (role != MembershipRole.OWNER
                && role != MembershipRole.ADMIN) {
            throw new IllegalArgumentException(
                    "Only project owners and admins can update tasks.");
        }

        // Apply the requested changes.
        existingTask.setTitle(request.getTitle());
        existingTask.setStatus(request.getStatus());

        Task savedTask = taskRepository.save(existingTask);

        return toResponse(savedTask);
    }

    // Delete a Task by its ID and indicate whether the Task existed.
    public boolean deleteTask(Long id, Long requestingUserId) {

        // Find the task.
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Task with ID " + id + " does not exist."));

        // Check the requester's membership in the task's project.
        Membership requesterMembership = membershipRepository
                .findByProjectIdAndUserId(
                        existingTask.getProject().getId(),
                        requestingUserId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "You must be a member of this project to delete its tasks."));

        // Only OWNERs and ADMINs can delete tasks.
        MembershipRole role = requesterMembership.getRole();

        if (role != MembershipRole.OWNER
                && role != MembershipRole.ADMIN) {
            throw new IllegalArgumentException(
                    "Only project owners and admins can delete tasks.");
        }

        taskRepository.delete(existingTask);

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