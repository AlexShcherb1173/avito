package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.skypro.homework.model.UserEntity;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends UserEntity {

    @Schema(description = "id пользователя")
    private Integer id;

    @Schema(description = "Логин пользователя")
    private String email;

    @Schema(description = "Имя пользователя")
    private String firstName;

    @Schema(description = "Фамилия пользователя")
    private String lastName;

    @Schema(description = "Телефон пользователя")
    private String phone;

    @Schema(examples = {"USER", "ADMIN"}, description = "Роль пользователя")
    private Role role;

    @Schema(description = "Ссылка на аватар пользователя")
    private String image;
}
