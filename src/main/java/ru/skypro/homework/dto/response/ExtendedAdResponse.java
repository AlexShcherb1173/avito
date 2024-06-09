package ru.skypro.homework.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExtendedAdResponse {
    /**
     * id объявления
     */
    @Schema(description = "id объявления")
    private Long pk;
    /**
     * имя автора объявления
     */
    @Schema(description = "имя автора объявления")
    private String authorFirstName;
    /**
     * фамилия автора объявления
     */
    @Schema(description = "фамилия автора объявления")
    private String authorLastName;
    /**
     * описание объявления
     */
    @Schema(description = "описание объявления")
    private String description;
    /**
     * логин автора объявления
     */
    @Schema(description = "логин автора объявления")
    private String email;
    /**
     * ссылка на картинку объявления
     */
    @Schema(description = "ссылка на картинку объявления")
    private String image;
    /**
     * телефон автора объявления
     */
    @Schema(description = "телефон автора объявления")
    private String phone;
    /**
     * цена объявления
     */
    @Schema(description = "цена объявления")
    private Integer price;
    /**
     * заголовок объявления
     */
    @Schema(description = "заголовок объявления")
    private String title;
}
