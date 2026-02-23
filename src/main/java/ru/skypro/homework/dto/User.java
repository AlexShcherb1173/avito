package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Пользователь")
public class User {

    @Schema(description = "id пользователя", example = "1")
    private Integer id;

    @Schema(description = "email пользователя", example = "user@gmail.com")
    private String email;

    @Schema(description = "имя пользователя", example = "Иван")
    private String firstName;

    @Schema(description = "фамилия пользователя", example = "Иванов")
    private String lastName;

    @Schema(description = "телефон пользователя", example = "+79991234567")
    private String phone;

    @Schema(description = "роль пользователя", example = "USER")
    private String role;

    @Schema(description = "ссылка на аватар", example = "/images/avatar.jpg")
    private String image;
}