package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Данные для входа")
public class Login {

    @Schema(description = "Имя пользователя (логин)", example = "user@example.com", required = true)
    private String username;

    @Schema(description = "Пароль", example = "P@ssw0rd", required = true)
    private String password;
}
