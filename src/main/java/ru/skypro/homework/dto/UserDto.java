package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Schema(type = "integer", format = "int32", description = "ID пользователя")
    @NotNull(message = "ID пользователя обязательно")
    private Integer id;

    @Schema(type = "string", description = "логин пользователя")
    @Email(message = "Некорректный формат электронной почты")
    @NotBlank(message = "Логин обязателен")
    private String email;

    @Schema(type = "string", description = "имя пользователя")
    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @Schema(type = "string", description = "фамилия пользователя")
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @Schema(type = "string", description = "телефон пользователя")
    @NotBlank(message = "Телефон обязателен")
    private String phone;

    @Schema(type = "string", description = "роль пользователя", allowableValues = {"USER", "ADMIN"})
    @NotNull(message = "Роль обязательна")
    private RoleDto roleDto;

    @Schema(type = "string", description = "ссылка на аватар пользователя")
    private String image;
}