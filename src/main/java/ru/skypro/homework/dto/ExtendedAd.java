package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class ExtendedAd {

    private Integer pk;             // идентификатор объявления
    private String authorFirstName; // имя автора объявления
    private String authorLastName;  // фамилия автора объявления
    private String description;     // описание товара
    private String email;           // электронная почта автора
    private String image;           // ссылка на изображение товара
    private String phone;           // номер телефона автора
    private Integer price;          // цена товара
    private String title;           // заголовок объявления

    public ExtendedAd() {
    }
}
