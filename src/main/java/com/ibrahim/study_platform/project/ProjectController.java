package com.ibrahim.study_platform.project;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import com.ibrahim.study_platform.project.dto.CreateProjectRequest;
import com.ibrahim.study_platform.project.dto.ProjectResponse;
import com.ibrahim.study_platform.project.dto.ProjectUpdateRequest;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ProjectResponse createProject(
            @RequestBody @Valid CreateProjectRequest request) {

        return projectService.createProject(
                request.getName(),
                request.getDescription(),
                request.getDeadline(),
                request.getCreatorId());
    }

    @GetMapping
    public List<ProjectResponse> getAllProjects(
            @RequestParam Long requestingUserId) {

        return projectService.getAllProjects(requestingUserId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(
            @PathVariable Long id,
            @RequestParam Long requestingUserId) {

        Optional<ProjectResponse> project =
                projectService.getProjectById(id, requestingUserId);

        return project.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @RequestParam Long requestingUserId,
            @RequestBody @Valid ProjectUpdateRequest request) {

        ProjectResponse project = projectService.updateProject(
                id, request, requestingUserId);

        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id,
            @RequestParam Long requestingUserId) {

        projectService.deleteProject(id, requestingUserId);

        return ResponseEntity.noContent().build();
    }
}