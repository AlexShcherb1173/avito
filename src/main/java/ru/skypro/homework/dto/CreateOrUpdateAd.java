package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CreateOrUpdateAd {

    private String title;       // Заголовок объявления
    private String price;       // Цена объявления
    private String description; // Описание объявления

}
