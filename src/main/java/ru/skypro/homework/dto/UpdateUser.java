package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные для обновления профиля пользователя")
public class UpdateUser {

    @Schema(description = "Имя", example = "Иван", required = true)
    private String firstName;

    @Schema(description = "Фамилия", example = "Иванов", required = true)
    private String lastName;

    @Schema(description = "Телефон", example = "+7 900 123-45-67", required = true)
    private String phone;
}
