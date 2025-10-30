package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель данных пользователя")
public class User {
    @Schema(
            type = "integer",
            format = "int32",
            description = "id пользователя"
    )
    private Integer id;

    @Schema(
            type = "string",
            description = "логин пользователя"
    )
    private String email;

    @Schema(
            type = "string",
            description = "имя пользователя"
    )
    private String firstName;

    @Schema(
            type = "string",
            description = "фамилия пользователя"
    )
    private String lastName;

    @Schema(
            type = "string",
            description = "телефон пользователя"
    )
    private String phone;

    @Schema(
            type = "string",
            description = "роль пользователя"
    )
    private Role role;

    @Schema(
            type = "string",
            description = "ссылка на аватар пользователя"
    )
    private String image;
}
