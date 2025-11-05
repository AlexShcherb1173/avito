package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;


@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель данных для регистрации нового пользователя")
public class Register {
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
    private String phone;
    @Schema(
            type = "string",
            description = "роль пользователя",
            example = "USER"
    )
    private Role role;

}
