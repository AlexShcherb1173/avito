package ru.skypro.homework.dto.response;
import ru.skypro.homework.dto.Role;

import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.parameters.ResponseEntity;
import io.swagger.v3.oas.annotations.Parameter;

@Data
@NoArgsConstructor
public class UserResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;

    public UserResponse(String username, String firstName, String lastName, String phone, Role role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
    }
}
