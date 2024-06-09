package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.annotation.validation.Password;

@Data
@Builder
public class Login {
    /**
     * логин пользователя
     */
    @Schema(description = "логин пользователя")
    @ru.skypro.homework.annotation.validation.Login
    private String username;
    /**
     * пароль пользователя
     */
    @Schema(description = "пароль пользователя")
    @Password
    private String password;
}
