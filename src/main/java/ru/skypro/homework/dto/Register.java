package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "DTO для регистрации")
@Data
public class Register {

    @Schema(description = "логин (min 4, max 32)")
    private String username;

    @Schema(description = "пароль (min 8, max 16)")
    private String password;

    @Schema(description = "имя пользователя (min 2, max 16)")
    private String firstName;

    @Schema(description = "фамилия пользователя (min 2, max 16)")
    private String lastName;

    @Schema(description = "телефон пользователя (+7 xxx xxx xx xx)")
    private String phone;

    @Schema(description = "роль пользователя (USER или ADMIN)")
    private Role role;


}
