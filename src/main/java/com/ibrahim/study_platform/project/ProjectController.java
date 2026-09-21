package com.ibrahim.study_platform.project;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ibrahim.study_platform.membership.Membership;
import com.ibrahim.study_platform.project.dto.CreateProjectRequest;
import com.ibrahim.study_platform.project.dto.ProjectResponse;
import com.ibrahim.study_platform.project.dto.ProjectUpdateRequest;
import com.ibrahim.study_platform.task.dto.TaskResponse;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @PostMapping
    public ProjectResponse createProject(@RequestBody CreateProjectRequest request) {
        return projectService.createProject(
                request.getName(),
                request.getDescription(),
                request.getDeadline(),
                request.getCreatorId()
        );
    }

    @GetMapping
    public List<ProjectResponse> getAllProjects(){
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        Optional<ProjectResponse> project = projectService.getProjectById(id);

        if(project.isPresent()){
            return ResponseEntity.ok(project.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @RequestBody @Valid ProjectUpdateRequest request) {

        ProjectResponse project = projectService.updateProject(id, request);

        if (project != null) {
            return ResponseEntity.ok(project);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        boolean deleted = projectService.deleteProject(id);

        if(deleted){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}