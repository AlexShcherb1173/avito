package ru.skypro.homework.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    /**
     * Логин пользователя.
     * Должен содержать от 4 до 32 символов.
     * Поле обязательно для заполнения.
     */
    @Size(min = 4, max = 32, message = "Логин должен содержать от 4 до 32 символов")
    @NotBlank(message = "Логин обязателен")
    private String username;

    /**
     * Пароль пользователя.
     * Должен содержать от 8 до 16 символов.
     * Поле обязательно для заполнения.
     */
    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    @NotBlank(message = "Пароль обязателен")
    private String password;
}