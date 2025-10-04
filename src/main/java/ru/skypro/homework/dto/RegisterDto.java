package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object для регистрации нового пользователя.
 * Если роль не указана, по умолчанию присваивается USER.
 */
@Schema(description = "User registration data")
public class RegisterDto {

    @Schema(description = "Username (email)")
    private String username;

    @Schema(description = "Password")
    private String password;

    @Schema(description = "First name")
    private String firstName;

    @Schema(description = "Last name")
    private String lastName;

    @Schema(description = "Phone number")
    private String phone;

    @Schema(description = "User role (по умолчанию USER)", example = "USER")
    private String role = "USER";

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
