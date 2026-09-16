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

    @Column(unique = true)
    private String username;

    @Column (unique = true)
    private String email;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<Membership> memberships = new ArrayList<Membership>();

    @OneToMany(mappedBy = "assignee")
    private List<Task> tasks = new ArrayList<Task>();

    protected User(){
    }


}
