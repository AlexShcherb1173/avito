package ru.skypro.homework.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "DTO for user registration")
@Data
public class Register {

    @Schema(description = "Unique username for the user", example = "john_doe")
    @Size(min=4,max=32)
    private String username;

    @Schema(description = "Password for the user account", example = "password123")
    @Size(min=8,max=16)
    private String password;

    @Schema(description = "User's first name", example = "John")
    @Size(min=2,max=16)
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    @Size(min=2,max=16)
    private String lastName;

    @Schema(description = "User's phone number", example = "+7 (123) 456-78-90")
    @Pattern(regexp = "^\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}$")
    private String phone;

    @Schema(description = "User's role in the system", example = "USER")
    private Role role;
}
