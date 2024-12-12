package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ExtendedAdDto {
    @Schema(type = "integer",
            description = "id объявления")
    public Integer pk;
    @Schema(type = "string",
            description = "имя автора объявления")
    public String authorFirstName;

    @Schema(type = "string",
            description = "фамилия автора объявления")
    public String authorLastName;

    @Schema(type = "string",
            description = " описание объявления ")
    public String description;



    @Schema(type = "string",
            description = " логин автора объявления ")
    public String email;

    @Schema(type = "string",
            description = " ссылка на картинку объявления ")
    public String image;

    @Schema(type = "string",
            description = " телефон автора объявления ")
    public String  phone;


    @Schema(type = "integer",
            description = "цена объявления")
    public Integer price;

    @Schema(type = "string",
            description = "заголовок объявления ")
    public String  title;

}
