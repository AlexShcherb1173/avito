package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Schema(name = "Login")
@Data
public class LoginDTO {
    @Size(min = 8, max = 16, message = "Длина пароля должна быть от 8 до 16 символов")
    private String password;

    @Size(min = 4, max = 32, message = "Длина логина должна быть от 4 до 32 символов")
    private String username;
}
