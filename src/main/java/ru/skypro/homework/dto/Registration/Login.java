package ru.skypro.homework.dto.Registration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Данные для входа пользователя
 */
@Data
@Schema(description = "Данные для входа пользователя")
public class Login {
    @Schema(description = "Логин пользователя (email)",
            example = "user@example.com")
    private String username = "";

    @Schema(description = "Пароль пользователя",
            example = "myPassword123")
    private String password = "";

}
