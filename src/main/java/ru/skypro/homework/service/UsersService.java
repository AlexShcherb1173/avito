package ru.skypro.homework.service;

import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.request.UpdateUserRequest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import ru.skypro.homework.dto.response.UserResponse; 
import ru.skypro.homework.dto.response.UpdateUserResponse;
import ru.skypro.homework.dto.Role;

@Service
public class UsersService {
    private final UserDetailsManager manager;
    public UsersService(UserDetailsManager manager){
        this.manager = manager;
    }

    public UserResponse getInfo() {
        UserResponse response = new UserResponse(
            "username", // Placeholder for username
            "firstName", // Placeholder for first name
            "lastName", // Placeholder for last name
            "phone", // Placeholder for phone number
            Role.USER // Assuming a default role, adjust as necessary
        );
        return response;
    }
    public UpdateUserResponse updateUser(UpdateUserRequest updateUser) {
        return new UpdateUserResponse(
            updateUser.getFirstName(),
            updateUser.getLastName(),
            updateUser.getPhone()
        );
    }

    public boolean setPassword(String currentPassword, String newPassword) {
        if (currentPassword != null && newPassword != null) {
            return true;
        }
        return false;
    }
}
