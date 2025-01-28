package ru.skypro.homework.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class RegisterDto {

    @Size(min = 4, max = 32, message = "Логин должен содержать от 4 до 32 символов")
    @NotBlank(message = "Логин обязателен")
    @Email(message = "Некорректный формат электронной почты")
    private String username;

    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    @NotBlank(message = "Пароль обязателен")
    private String password;

    @Size(min = 2, max = 16, message = "Имя должно содержать от 2 до 16 символов")
    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @Size(min = 2, max = 16, message = "Фамилия должна содержать от 2 до 16 символов")
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", message = "Номер телефона должен соответствовать формату +7 (XXX) XXX-XX-XX")
    @NotBlank(message = "Телефон обязателен")
    private String phone;

    @NotNull(message = "Роль обязательна")
    private RoleDto role;
}