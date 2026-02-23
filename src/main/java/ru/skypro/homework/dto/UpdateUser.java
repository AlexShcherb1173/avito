package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Обновление пользователя")
public class UpdateUser {

    @Schema(description = "имя", example = "Иван")
    private String firstName;

    @Schema(description = "фамилия", example = "Иванов")
    private String lastName;

    @Schema(description = "телефон", example = "+79991234567")
    private String phone;
}