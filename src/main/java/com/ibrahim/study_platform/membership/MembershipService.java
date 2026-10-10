package com.ibrahim.study_platform.membership;

import org.springframework.stereotype.Service;

import com.ibrahim.study_platform.membership.MembershipRepository;
import com.ibrahim.study_platform.membership.MembershipRole;
import com.ibrahim.study_platform.membership.dto.MembershipResponse;
import com.ibrahim.study_platform.user.UserRepository;

import jakarta.transaction.Transactional;

import com.ibrahim.study_platform.user.User;
import com.ibrahim.study_platform.project.ProjectRepository;
import com.ibrahim.study_platform.project.Project;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    // Creates the initial OWNER membership when a Project is created.
    public MembershipResponse createOwnerMembership(Long projectId, Long creatorId) {

        // Retrieve the Project.
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Project with ID " + projectId + " does not exist."));

        // Retrieve the creator.
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User with ID " + creatorId + " does not exist."));

        // Prevent duplicate memberships.
        if (membershipRepository.existsByProjectIdAndUserId(projectId, creatorId)) {
            throw new IllegalArgumentException(
                    "User with ID " + creatorId +
                    " is already a member of project with ID " + projectId);
        }

        // A project must not have more than one OWNER.
        if (membershipRepository.existsByProjectId(projectId)) {
            throw new IllegalArgumentException(
                    "Project with ID " + projectId +
                    " already has members. An initial OWNER cannot be created.");
        }

        Membership membership = new Membership();
        membership.setRole(MembershipRole.OWNER);
        membership.setJoinedAt(LocalDateTime.now());
        membership.setProject(project);
        membership.setUser(creator);

        Membership savedMembership = membershipRepository.save(membership);

        return toResponse(savedMembership);
    }

        // Adds a MEMBER to an existing Project after checking authorization.
    public MembershipResponse addMember(
            Long projectId, Long userId, Long requestingUserId) {

        // Retrieve the Project.
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Project with ID " + projectId + " does not exist."));

        // Retrieve the User being added.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User with ID " + userId + " does not exist."));

        // Prevent duplicate memberships.
        if (membershipRepository.existsByProjectIdAndUserId(projectId, userId)) {
            throw new IllegalArgumentException(
                    "User with ID " + userId +
                    " is already a member of project with ID " + projectId);
        }

        // Find the requesting user's membership.
        Membership requestingMembership =
                getMembershipForUser(projectId, requestingUserId);

        // Only OWNER and ADMIN may add members.
        if (requestingMembership.getRole() == MembershipRole.MEMBER) {
            throw new IllegalArgumentException(
                    "User with ID " + requestingUserId +
                    " is not allowed to add this member.");
        }

        // Every member added to an existing project starts as MEMBER.
        Membership membership = new Membership();
        membership.setRole(MembershipRole.MEMBER);
        membership.setJoinedAt(LocalDateTime.now());
        membership.setProject(project);
        membership.setUser(user);

        Membership savedMembership = membershipRepository.save(membership);

        return toResponse(savedMembership);
    }

    public List<MembershipResponse> getAllMembers(){
        List<Membership> memberList = membershipRepository.findAll();
        return memberList.stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<MembershipResponse> getMemberById(Long id){
        return membershipRepository.findById(id)
                .map(this::toResponse);
    }

    public boolean deleteMember(Long id){
        Optional<Membership> member = membershipRepository.findById(id);

        if(member.isEmpty()){
            return false;
        }

        membershipRepository.deleteById(id);

        return true;
    }

    private MembershipResponse toResponse(Membership member){
        MembershipResponse response = new MembershipResponse();
        
        response.setId(member.getId());
        response.setRole(member.getRole());
        response.setJoinedAt(member.getJoinedAt());
        response.setProjectId(member.getProject().getId());
        if(member.getUser() != null){
            response.setUserId(member.getUser().getId());
        }

        return response;
    }

    public Membership getMembershipForUser(Long projectId, Long userId) {
        return membershipRepository.findByProjectIdAndUserId(projectId, userId)
            .orElseThrow(() -> new IllegalArgumentException(
                "No membership found for project ID " + projectId +
                " and user ID " + userId));
    }

    public void removeMember(Long requestingUserId, Long membershipId, Long projectId){
        Membership requestingMembership = getMembershipForUser(projectId, requestingUserId);
        Membership targetMembership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Membership with ID " + membershipId + " does not exist."));

        if (!targetMembership.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException(
                    "Membership with ID " + membershipId + " does not belong to project ID " + projectId);
        }

        MembershipRole requesterRole = requestingMembership.getRole();
        MembershipRole targetRole = targetMembership.getRole(); 

        //Using role names instead of ordinal in case additional roles are added later.

        if (requesterRole != MembershipRole.OWNER
                && requesterRole != MembershipRole.ADMIN) {
            throw new IllegalArgumentException(
                    "User with ID " + requestingUserId +
                    " is not allowed to remove this member.");
        }

        if (requesterRole == MembershipRole.ADMIN
                && targetRole != MembershipRole.MEMBER) {
            throw new IllegalArgumentException(
                    "ADMIN users can only remove MEMBER users.");
        }

        if (targetRole == MembershipRole.OWNER) {
            throw new IllegalArgumentException(
                    "The project OWNER cannot be removed.");
        }

        deleteMember(targetMembership.getId());
    }

    public MembershipResponse promoteMember(Long projectId, Long requestingUserId, Long membershipId){
        Membership requestingMembership = getMembershipForUser(projectId, requestingUserId);
        Membership targetMembership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Membership with ID " + membershipId + " does not exist."));

        if (!targetMembership.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException(
                    "Membership with ID " + membershipId + " does not belong to project ID " + projectId);
        }

        MembershipRole requesterRole = requestingMembership.getRole();
        MembershipRole targetRole = targetMembership.getRole();

        if (requesterRole != MembershipRole.OWNER
                && requesterRole != MembershipRole.ADMIN) {
            throw new IllegalArgumentException(
                    "User with ID " + requestingUserId +
                    " is not allowed to promote members.");
        }

        if (targetRole != MembershipRole.MEMBER) {
            throw new IllegalArgumentException(
                    "Only MEMBERS can be promoted to ADMIN.");
        }
            
        targetMembership.setRole(MembershipRole.ADMIN);
        Membership savedMembership = membershipRepository.save(targetMembership);
        return toResponse(savedMembership);
    }

    public MembershipResponse demoteMember(Long projectId, Long requestingUserId, Long membershipId){
        Membership requestingMembership = getMembershipForUser(projectId, requestingUserId);
        Membership targetMembership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Membership with ID " + membershipId + " does not exist."));

        if (!targetMembership.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException(
                    "Membership with ID " + membershipId + " does not belong to project ID " + projectId);
        }

        if (requestingMembership.getRole() != MembershipRole.OWNER) {
    throw new IllegalArgumentException(
            "Only the OWNER can demote admins.");
        }

        if (targetMembership.getRole() != MembershipRole.ADMIN) {
            throw new IllegalArgumentException(
                    "Only ADMINS can be demoted.");
        }

        targetMembership.setRole(MembershipRole.MEMBER);
        Membership savedMembership = membershipRepository.save(targetMembership);
        return toResponse(savedMembership);
    }

    @Transactional 
    public List<MembershipResponse> transferOwnership(Long projectId, Long requestingUserId, Long membershipId){
        Membership requestingMembership = getMembershipForUser(projectId, requestingUserId);
        Membership targetMembership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Membership with ID " + membershipId + " does not exist."));

        if (!targetMembership.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException(
                    "Membership with ID " + membershipId + " does not belong to project ID " + projectId);
        }

        if (requestingMembership.getRole() != MembershipRole.OWNER) {
    throw new IllegalArgumentException(
            "Only the OWNER can transfer ownership.");
        }

        if (requestingMembership.getId() == targetMembership.getId()){
    throw new IllegalArgumentException(
        "You cannot transfer ownership to yourself");
        }

        requestingMembership.setRole(targetMembership.getRole());
        targetMembership.setRole(MembershipRole.OWNER);

        Membership previousOwner = membershipRepository.save(requestingMembership);
        Membership newOwner = membershipRepository.save(targetMembership);

        return List.of(
            toResponse(previousOwner),
            toResponse(newOwner)
        );
    }

}

