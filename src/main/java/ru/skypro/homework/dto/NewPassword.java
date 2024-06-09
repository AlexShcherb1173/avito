package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.skypro.homework.annotation.validation.Password;

/**
 * DTO для обновления пароля
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewPassword {
    /**
     * текущий пароль
     */
    @Schema(description = "текущий пароль")
    @Password
    private String currentPassword;
    /**
     * новый пароль
     */
    @Schema(description = "новый пароль")
    @Password
    private String newPassword;
}
