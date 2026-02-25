package ru.skypro.homework.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "DTO для обновления данных пользователя")
@Data
public class UpdateUser {

    @Schema(description = "имя пользователя (min 3, max 10)")
    private String firstName;

    @Schema(description = "фамилия пользователя (min 3, max 10)")
    private String lastName;

    @Schema(description = "телефон пользователя")
    private String phone;

}
