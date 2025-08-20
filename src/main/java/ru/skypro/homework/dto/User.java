package ru.skypro.homework.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import ru.skypro.homework.dto.Role;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User entity representing a user in the system")
@Data
@AllArgsConstructor
public class User{
    @Schema(description = "Unique username", example = "john_doe")
    private String username;
    @Schema(description = "User's password", example = "password123")
    private String password;
    @Schema(description = "User's first name", example = "John")
    private String firstName;
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;
    @Schema(description = "User's Phone number", example = "+1234567890")
    private String phone;
    @Schema(description = "User's role", example = "USER")
    private Role role;

}
