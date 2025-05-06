package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Schema(name = "Register")
@Data
public class RegisterDTO {

    @Size(min = 4, max = 32, message = "Длина логина должна быть от 4 до 32 символов")
    private String username;

    @Size(min = 8, max = 16, message = "Длина пароля должна быть от 8 до 16 символов")
    private String password;

    @Size(min = 2, max = 16, message = "Длина имени должна быть от 2 до 16 символов")
    private String firstName;

    @Size(min = 2, max = 16, message = "Длина фамилии должна быть от 2 до 16 символов")
    private String lastName;

    @Pattern(regexp = "^\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}$",
            message = "Номер телефона должен соответствовать формату +7 XXX XXX-XX-XX")
    private String phone;

    @NotNull(message = "Роль должна быть определена")
    private Role role;
}
