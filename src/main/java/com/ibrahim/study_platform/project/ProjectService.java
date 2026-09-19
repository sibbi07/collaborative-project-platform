package com.ibrahim.study_platform.project;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import com.ibrahim.study_platform.membership.MembershipService;

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
    public Project createProject(String name, String description, LocalDateTime deadline, Long creatorId){

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

        return savedProject;
    }
}