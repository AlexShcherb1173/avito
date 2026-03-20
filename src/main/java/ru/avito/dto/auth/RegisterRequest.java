package ru.avito.dto.auth;

import lombok.Data;
import ru.avito.entity.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {

    @Email(message = "Email has invalid format")
    @NotBlank(message = "Email must not be blank")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 4, message = "Password must contain at least 4 characters")
    private String password;

    @NotBlank(message = "First name must not be blank")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    @NotBlank(message = "Phone must not be blank")
    private String phone;

    private Role role;
}