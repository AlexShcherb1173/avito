package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Класс, представляющий собой Data Transfer Object (DTO) для пользователя.
 * Включает информацию о пользователе, включая ID, логин, имя, фамилию, телефон, роль и ссылку на аватар.
 */

@Data
public class UserDTO {

    @Schema(description = "ID пользователя")
    @NotNull(message = "ID пользователя не должен быть пустым")
    private int id;

    @Schema(description = "Логин", minLength = 4, maxLength = 32)
    @Size(min = 4, max = 32, message = "Логин должен содержать от 4 до 32 символов")
    @NotBlank(message = "Логин не должен быть пустым")
    private String username;

    @Schema(description = "Имя пользователя")
    @Size(min = 2, max = 32, message = "Имя должно содержать от 2 до 32 символов")
    private String firstName;

    @Schema(description = "Фамилия пользователя")
    @Size(min = 2, max = 32, message = "Фамилия должна содержать от 2 до 32 символов")
    private String lastName;

    @Schema(description = "Телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;

    @Schema(description = "Роль пользователя", example = "USER")
    private RoleDTO role;

    @Schema(description = "Ссылка на аватар пользователя")
    private String image;
}
