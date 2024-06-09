package ru.skypro.homework.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.skypro.homework.annotation.validation.Login;
import ru.skypro.homework.annotation.validation.*;
import ru.skypro.homework.jackson.deserializer.RoleUserDeserializer;

/**
 * DTO для регистрации нового пользователя
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Register {
    /**
     * логин пользователя
     */
    @Schema(description = "логин пользователя")
    @Login
    private String username;
    /**
     * пароль пользователя
     */
    @Schema(description = "пароль пользователя")
    @Password
    private String password;
    /**
     * имя пользователя
     */
    @Schema(description = "имя пользователя")
    @FirstName
    private String firstName;
    /**
     * фамилия пользователя
     */
    @Schema(description = "фамилия пользователя")
    @LastName
    private String lastName;
    /**
     * телефон пользователя
     */
    @Schema(description = "телефон пользователя")
    @MobilePhone
    private String phone;
    /**
     * роль пользователя
     */
    @Schema(description = "роль пользователя")
    @JsonDeserialize(using = RoleUserDeserializer.class)
    @RoleUser
    private Role role;
}
