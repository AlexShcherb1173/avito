package ru.skypro.homework.dto.Registration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.dto.Role;

@Data
public class Register {

    @Schema(description = "Email пользователя",
            example = "user@example.com")
    private String username = "";

    @Schema(description = "Пароль пользователя",
            example = "password",
            minLength = 8)
    private String password = "";

    @Schema(description = "Имя пользователя",
            example = "Иван")
    private String firstName = "";

    @Schema(description = "Фамилия пользователя",
            example = "Иванов")
    private String lastName = "";

    @Schema(description = "Телефон пользователя в формате +7XXXYYYZZZZ",
            example = "+79991234567")
    private String phone = "";

    @Schema(description = "Роль пользователя",
            allowableValues = {"USER", "ADMIN"},
            defaultValue = "USER")
    private Role role = Role.USER ;

}
