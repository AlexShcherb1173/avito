package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "NewPassword")
@Data
public class NewPasswordDTO {
    @Size(min = 8, max = 16, message = "Длина пароля должна быть от 8 до 16 символов")
    private String currentPassword;

    @Size(min = 8, max = 16, message = "Длина пароля должна быть от 8 до 16 символов")
    private String newPassword;
}
