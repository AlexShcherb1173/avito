package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Данные для обновления информации о пользователе")
public class UpdateUser {

    @Schema(description = "Имя пользователя")
    private String firstName = "";

    @Schema(description = "Фамилия пользователя")
    private String lastName = "";

    @Schema(description = "Телефон в формате +7XXXYYYZZZZ",
            example = "+79991234567")
    private String phone = "";
}
