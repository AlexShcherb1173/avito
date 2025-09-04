package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Публичные данные пользователя")
public class User {

    @Schema(description = "Идентификатор пользователя", example = "7", required = true)
    private Integer id;

    @Schema(description = "Email пользователя", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "Имя", example = "Иван", required = true)
    private String firstName;

    @Schema(description = "Фамилия", example = "Иванов", required = true)
    private String lastName;

    @Schema(description = "Телефон", example = "+7 900 123-45-67")
    private String phone;

    @Schema(description = "Роль пользователя", required = true)
    private Role role;

    @Schema(description = "URL аватарки", example = "/images/users/7.png")
    private String image;
}
