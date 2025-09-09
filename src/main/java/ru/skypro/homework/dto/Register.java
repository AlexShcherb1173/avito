package ru.skypro.homework.dto;

/**
 * отвечает за получение и валидацию данных при регистрации нового пользователя
 */


import lombok.Data;
import jakarta.validation.constraints.*;


@Data
public class Register {
    @NotBlank
    @Size(min = 4, max = 32)
    private String username;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;

    @NotBlank
    @Size(min = 2, max = 16)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 16)
    private String lastName;

    @Pattern(regexp = "\\+7\\s?\\(\\d{3}\\)\\s?\\d{3}-\\d{2}-\\d{2}")
    private String phone;

    private Role role = Role.USER; // по умолчанию USER

    public @NotBlank @Size(min = 4, max = 32) String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank @Size(min = 4, max = 32) String username) {
        this.username = username;
    }

    public @NotBlank @Size(min = 8, max = 16) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8, max = 16) String password) {
        this.password = password;
    }

    public @NotBlank @Size(min = 2, max = 16) String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank @Size(min = 2, max = 16) String firstName) {
        this.firstName = firstName;
    }

    public @Pattern(regexp = "\\+7\\s?\\(\\d{3}\\)\\s?\\d{3}-\\d{2}-\\d{2}") String getPhone() {
        return phone;
    }

    public void setPhone(@Pattern(regexp = "\\+7\\s?\\(\\d{3}\\)\\s?\\d{3}-\\d{2}-\\d{2}") String phone) {
        this.phone = phone;
    }

    public @NotBlank @Size(min = 2, max = 16) String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank @Size(min = 2, max = 16) String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}