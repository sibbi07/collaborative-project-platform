package com.ibrahim.study_platform.project;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

import com.ibrahim.study_platform.membership.MembershipService;
import com.ibrahim.study_platform.project.dto.ProjectResponse;
import com.ibrahim.study_platform.project.dto.ProjectUpdateRequest;

import java.util.List;
import java.util.Optional;


@Service
public class ProjectService { 

    private final ProjectRepository projectRepository;
    private final MembershipService membershipService;

    // Inject the services required to create Projects and add the creator as a Project member.
    public ProjectService(ProjectRepository projectRepository, MembershipService membershipService){
        this.projectRepository = projectRepository;
        this.membershipService = membershipService;
    }

    // Ensure both the Project creation and creator Membership are completed as one database transaction.
    @Transactional 
    public ProjectResponse createProject(String name, String description, LocalDateTime deadline, Long creatorId){

        Project project = new Project();
        project.setName(name); 
        project.setDescription(description); 
        project.setDeadline(deadline);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        
        // Save the Project first so that its generated ID can be used when creating the Membership.
        Project savedProject = projectRepository.save(project);

        Long projectId = savedProject.getId();

        // Add the creator as the first member of the Project.
        // MembershipService will assign them the OWNER role.
        membershipService.addMember(projectId, creatorId);

        return toResponse(savedProject);
    }

    public List<ProjectResponse> getAllProjects(){
        List<Project> projectList = projectRepository.findAll();
        return projectList.stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<ProjectResponse> getProjectById(Long id){
        return projectRepository.findById(id)
                .map(this::toResponse);
    }

    public ProjectResponse updateProject(Long id, ProjectUpdateRequest request) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isEmpty()) {
            return null;
        }

        Project existingProject = project.get();

        existingProject.setName(request.getName());
        existingProject.setDescription(request.getDescription());
        existingProject.setDeadline(request.getDeadline());

        Project savedProject = projectRepository.save(existingProject);

        return toResponse(savedProject);
    }

    public boolean deleteProject(Long id){
        Optional<Project> project = projectRepository.findById(id);

        if(project.isEmpty()){
            return false;
        }

        projectRepository.deleteById(id);

        return true;
    }

    private ProjectResponse toResponse(Project project){
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