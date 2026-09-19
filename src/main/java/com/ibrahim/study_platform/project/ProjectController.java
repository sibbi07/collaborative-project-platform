package com.ibrahim.study_platform.project;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ibrahim.study_platform.project.dto.CreateProjectRequest;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @PostMapping
    public Project createProject(@RequestBody CreateProjectRequest request) {
        return projectService.createProject(
                request.getName(),
                request.getDescription(),
                request.getDeadline(),
                request.getCreatorId()
        );
    }
}