package com.ibrahim.study_platform.user;

import com.ibrahim.study_platform.membership.Membership;
import com.ibrahim.study_platform.task.Task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name="users")
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

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public List<Membership> getMemberships(){
        return memberships;
    }

    public void setMemberships(List<Membership> memberships){
        this.memberships = memberships;
    }

    public List<Task> getTasks(){
        return tasks;
    }

    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
    }
}