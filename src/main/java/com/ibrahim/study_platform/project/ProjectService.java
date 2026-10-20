package com.ibrahim.study_platform.project;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ibrahim.study_platform.membership.Membership;
import com.ibrahim.study_platform.membership.MembershipRepository;
import com.ibrahim.study_platform.membership.MembershipRole;
import com.ibrahim.study_platform.membership.MembershipService;
import com.ibrahim.study_platform.project.dto.ProjectResponse;
import com.ibrahim.study_platform.project.dto.ProjectUpdateRequest;
import com.ibrahim.study_platform.task.TaskRepository;

@Service
public class ProjectService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipService membershipService;

    public ProjectService(
            ProjectRepository projectRepository,
            MembershipRepository membershipRepository,
            MembershipService membershipService,
            TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.membershipRepository = membershipRepository;
        this.membershipService = membershipService;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public ProjectResponse createProject(
            String name,
            String description,
            LocalDateTime deadline,
            Long creatorId) {

        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setDeadline(deadline);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        Project savedProject = projectRepository.save(project);

        membershipService.createOwnerMembership(
                savedProject.getId(), creatorId);

        return toResponse(savedProject);
    }

    public List<ProjectResponse> getAllProjects(Long requestingUserId) {

        List<Project> projects =
                projectRepository.findDistinctByMembershipsUserId(
                        requestingUserId);

        return projects.stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<ProjectResponse> getProjectById(
            Long id, Long requestingUserId) {

        Optional<Project> project = projectRepository.findById(id);

        if (project.isEmpty()) {
            return Optional.empty();
        }

        membershipRepository.findByProjectIdAndUserId(
                id, requestingUserId
        ).orElseThrow(() -> new IllegalArgumentException(
                "You must be a member of this project to view it."));

        return project.map(this::toResponse);
    }

    public ProjectResponse updateProject(
            Long id,
            ProjectUpdateRequest request,
            Long requestingUserId) {

        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Project with ID " + id + " does not exist."));

        Membership membership = membershipRepository
                .findByProjectIdAndUserId(id, requestingUserId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "You must be a member of this project to update it."));

        MembershipRole role = membership.getRole();

        if (role != MembershipRole.OWNER
                && role != MembershipRole.ADMIN) {
            throw new IllegalArgumentException(
                    "Only project owners and admins can update projects.");
        }

        existingProject.setName(request.getName());
        existingProject.setDescription(request.getDescription());
        existingProject.setDeadline(request.getDeadline());
        existingProject.setUpdatedAt(LocalDateTime.now());

        Project savedProject = projectRepository.save(existingProject);

        return toResponse(savedProject);
    }

    @Transactional
    public void deleteProject(Long id, Long requestingUserId) {
        projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Project with ID " + id + " does not exist."));

        Membership membership = membershipRepository
                .findByProjectIdAndUserId(id, requestingUserId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "You must be a member of this project to delete it."));

        if (membership.getRole() != MembershipRole.OWNER) {
                throw new IllegalArgumentException(
                        "Only the project owner can delete the project.");
        }

        taskRepository.deleteByProjectId(id);
        membershipRepository.deleteByProjectId(id);
        projectRepository.deleteById(id);
        }

    private ProjectResponse toResponse(Project project) {
        ProjectResponse response = new ProjectResponse();

        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setDeadline(project.getDeadline());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());

        return response;
    }
}