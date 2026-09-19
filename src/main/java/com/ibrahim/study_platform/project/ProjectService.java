package com.ibrahim.study_platform.project;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import com.ibrahim.study_platform.membership.MembershipService;

@Service
public class ProjectService { 

    private final ProjectRepository projectRepository;
    private final MembershipService membershipService;

    public ProjectService(ProjectRepository projectRepository, MembershipService membershipService){
        this.projectRepository = projectRepository;
        this.membershipService = membershipService;
    }

    @Transactional 
    public Project createProject(String name, String description, LocalDateTime deadline, Long creatorId){

        Project project = new Project();
        project.setName(name); 
        project.setDescription(description); 
        project.setDeadline(deadline);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        
        Project savedProject = projectRepository.save(project);

        Long projectId = savedProject.getId();
        membershipService.addMember(projectId, creatorId);

        return savedProject;
    }
}
