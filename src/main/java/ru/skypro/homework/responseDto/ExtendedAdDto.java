package ru.skypro.homework.responseDto;

import lombok.Data;

// DTO для расширенной информации об объявлении.
// Содержит полные данные объявления включая информацию об авторе.

@Data
public class ExtendedAdDto {
    // ID объявления
    private Integer pk;

    // Имя автора
    private String authorFirstName;

    // Фамилия автора
    private String authorLastName;

    // Описание объявления
    private String description;

    // Email автора
    private String email;

    // URL изображения объявления
    private String image;

    // Телефон автора
    private String phone;

    // Цена объявления
    private Integer price;

    // Заголовок объявления
    private String title;
}