package com.ibrahim.study_platform.user;

import org.springframework.stereotype.Service;

import com.ibrahim.study_platform.project.Project;
import com.ibrahim.study_platform.user.dto.UserResponse;
import com.ibrahim.study_platform.user.dto.UserUpdateRequest;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(String username, String email, String password){
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    public List<UserResponse> getAllUsers(){
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<UserResponse> getUserById(Long id){
        return userRepository.findById(id)
                .map(this::toResponse);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return null;
        }

        User existingUser = user.get();

        existingUser.setUsername(request.getUsername());
        existingUser.setEmail(request.getEmail());

        User savedUser = userRepository.save(existingUser);

        return toResponse(savedUser);
    }

    public boolean deleteUser(Long id){
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty()){
            return false;
        }

        userRepository.deleteById(id);

        return true;
    }

    private UserResponse toResponse(User user){
        UserResponse response = new UserResponse();
        
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }
}