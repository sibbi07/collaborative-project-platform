package com.ibrahim.study_platform.membership;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ibrahim.study_platform.membership.MembershipService;
import com.ibrahim.study_platform.membership.dto.CreateMembershipRequest;
import com.ibrahim.study_platform.membership.dto.MembershipResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PutMapping;




@RequestMapping("/memberships")
@RestController 
public class MembershipController {
    private final MembershipService membershipService;
    
    public MembershipController(MembershipService membershipService){
        this.membershipService = membershipService;
    }

    @PostMapping
    public MembershipResponse addMember(@RequestBody CreateMembershipRequest request) {
        return membershipService.addMember(
                request.getProjectId(),
                request.getUserId(),
                request.getRequestingUserId()
        );
    }

    @GetMapping
    public List<MembershipResponse> getAllMembers() {
        return membershipService.getAllMembers();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MembershipResponse> getMemeberById(@PathVariable Long id) {
        Optional<MembershipResponse> member = membershipService.getMemberById(id);

        if(member.isPresent()){
            return ResponseEntity.ok(member.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{membershipId}/promote")
    public MembershipResponse promoteMember(
            @PathVariable Long membershipId,
            @RequestParam Long projectId,
            @RequestParam Long requestingUserId) {

        return membershipService.promoteMember(
                projectId,
                requestingUserId,
                membershipId
        );
    }

    @PutMapping("/{membershipId}/demote")
    public MembershipResponse demoteMember(
            @PathVariable Long membershipId,
            @RequestParam Long projectId,
            @RequestParam Long requestingUserId) {

        return membershipService.demoteMember(
                projectId,
                requestingUserId,
                membershipId
        );
    }


    @PutMapping("/{membershipId}/transfer-ownership")
    public List<MembershipResponse> transferOwnership(
            @PathVariable Long membershipId,
            @RequestParam Long projectId,
            @RequestParam Long requestingUserId) {

        return membershipService.transferOwnership(
                projectId,
                requestingUserId,
                membershipId
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(
                @RequestParam Long requestingId, 
                @PathVariable Long membershipId, 
                @PathVariable Long projectId){
        
        
        membershipService.removeMember(requestingId, membershipId, projectId);
        
        return ResponseEntity.noContent().build();
    }
    
}
