```java
package com.ibrahim.study_platform.membership;

import org.springframework.stereotype.Service;

import com.ibrahim.study_platform.membership.MembershipRepository;
import com.ibrahim.study_platform.membership.MembershipRole;
import com.ibrahim.study_platform.user.UserRepository;
import com.ibrahim.study_platform.user.User;
import com.ibrahim.study_platform.project.ProjectRepository;
import com.ibrahim.study_platform.project.Project;
import java.time.LocalDateTime;

@Service
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    // Inject the repositories required to retrieve Users and Projects and manage Memberships.
    public MembershipService(MembershipRepository membershipRepository, UserRepository userRepository, ProjectRepository projectRepository){
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    // Adds a User to a Project and determines their role based on whether they are the first member.
    public Membership addMember(Long projectId, Long userId) {

        // Retrieve the Project. Throw an exception if the specified Project does not exist.
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + projectId + " does not exist."));

        // Retrieve the User. Throw an exception if the specified User does not exist.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));

        // Prevent the same User from being added to the same Project more than once.
        if(membershipRepository.existsByProjectIdAndUserId(projectId, userId)){
            throw new IllegalArgumentException("User with ID " + userId + " is already a member of project with ID " + projectId);
        }

        Membership membership = new Membership();

        // The first member of a Project automatically becomes its owner.
        // All subsequent members are assigned the normal MEMBER role.
        if(!membershipRepository.existsByProjectId(projectId)){
            membership.setRole(MembershipRole.OWNER);
        }
        else{
            membership.setRole(MembershipRole.MEMBER);
        }

        // Record when the User joined the Project.
        membership.setJoinedAt(LocalDateTime.now());

        // Associate the Membership with the User and Project.
        membership.setProject(project);
        membership.setUser(user);

        // Save the new Membership to the database and return the persisted entity.
        return membershipRepository.save(membership);
    }
}

