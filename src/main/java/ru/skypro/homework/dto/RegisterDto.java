package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.entity.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель данных для регистрации нового пользователя")
public class RegisterDto {
    @Schema(
            type = "string",
            description = "логин"
    )
    @Size(min = 4, max = 32)
    private String username;

    @Schema(
            type = "string",
            description = "пароль"
    )
    @Size(min = 8, max = 16)
    private String password;

    @Schema(
            type = "string",
            description = "имя пользователя"
    )
    @Size(min = 2, max = 16)
    private String firstName;

    @Schema(
            type = "string",
            description = "фамилия пользователя"
    )
    @Size(min = 2, max = 16)
    private String lastName;

    @Schema(
            type = "string",
            description = "телефон пользователя",
            pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            example = "string"
    )
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    @NotBlank
    private String phone;

    @Schema(
            type = "string",
            description = "роль пользователя",
            example = "USER"
    )

    private Role role;
}
