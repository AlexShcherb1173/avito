package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Ad {

    private Integer author;  // id автора объявления
    private String image;    // Ссылка на картинку объявления
    private Integer pk;      // id объявления
    private Integer price;   // Цена объявления
    private String title;    // Заголовок объявления

    public Ad() {
    }
}
