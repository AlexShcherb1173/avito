package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.entity.Role;

@Data
public class UserDto {

    @Schema(description = "id пользователя")
    private int id;

    @Schema(description = "логин пользователя")
    private String email;

    @Schema(description = "имя пользователя")
    private String firstName;

    @Schema(description = "фамилия пользователя")
    private String lastName;

    @Schema(description = "телефон пользователя")
    private String phone;

    @Schema(description = "роль пользователя",
            allowableValues = {"USER", "ADMIN"})
    private Role role;

    @Schema(description = "ссылка на автора пользователя")
    private String image;
}
