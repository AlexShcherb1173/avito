package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель данных для смены пароля")
public class NewPassword {
    @Schema(
            type = "string",
            description = "текущий пароль"
    )
    @Size(min = 8, max = 16)
    private String currentPassword;

    @Schema(
            type = "string",
            description = "новый пароль"
    )
    @Size(min = 8, max = 16)
    private String newPassword;
}