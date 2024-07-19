package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class User {

    @Schema(description = "id пользователя",
            required = true)
    private int id;

    @Schema(description = "логин пользователя",
            required = true)
    private String email;

    @Schema(description = "имя пользователя",
            required = true)
    private String firstName;

    @Schema(description = "фамилия пользователя",
            required = true)
    private String lastName;

    @Schema(description = "телефон пользователя",
            required = true)
    private String phone;

    @Schema(description = "роль пользователя",
            required = true,
            allowableValues = {"USER", "ADMIN"})
    private Role role;

    @Schema(description = "ссылка на автора пользователя",
            required = true)
    private String image;
}
