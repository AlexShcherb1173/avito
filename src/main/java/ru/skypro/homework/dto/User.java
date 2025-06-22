package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Информация о пользователе системы")
/**
 * Информация о пользователе
 */
public class User {

    @Schema(description = "ID пользователя")
    private Integer id = 0;

    @Schema(description = "Email пользователя")
    private String email = "";

    @Schema(description = "Имя пользователя")
    private String firstName = "";

    @Schema(description = "Фамилия пользователя")
    private String lastName = "";

    @Schema(description = "Телефон в формате +7XXXYYYZZZZ")
    private String phone = "";

    @Schema(description = "Роль пользователя",
            allowableValues = {"USER", "ADMIN"})
    private Role role = Role.USER;

    @Schema(description = "Ссылка на аватар",
            example = "/users/image/123")
    private String image = "";

}
