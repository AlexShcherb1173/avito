package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Ad {
    //                              Объявление (DTO)
    private int author;         //  id автора объявления
    private String image;       //  Ссылка на картинку объявления
    private int pk;             //  id объявления
    private int price;          //  Цена объявления
    private String title;       //  Заголовок объявления

}
