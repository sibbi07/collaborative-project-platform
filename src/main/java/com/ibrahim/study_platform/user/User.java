package com.ibrahim.study_platform.user;

import com.ibrahim.study_platform.membership.Membership;
import com.ibrahim.study_platform.task.Task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.ArrayList;

@Entity
public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usernames and email addresses must be unique across all Users.
    @Column(unique = true)
    private String username;

    @Column (unique = true)
    private String email;

    private String password;

    // Store all Project Memberships associated with this User.
    // The Membership entity owns the relationship through its "user" field.
    @OneToMany(mappedBy="user")
    private List<Membership> memberships = new ArrayList<Membership>();

    // Store all Tasks currently assigned to this User.
    // The Task entity owns the relationship through its "assignee" field.
    @OneToMany(mappedBy="assignee")
    private List<Task> tasks = new ArrayList<Task>();

    // Protected no-argument constructor required by JPA when creating User entities.
    protected User(){
    }
}