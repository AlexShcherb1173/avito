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
@Schema(description = "Модель данных для логина и пароля")
public class LoginDto {
    @Schema(
            type = "string",
            description = "пароль",
            example = "stringst"
    )
    @Size(min = 8, max = 16)
    private String password;

    @Schema(
            type = "string",
            description = "логин"
    )
    @Size(min = 4, max = 32)
    private String username;
}
