package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Ad {
    /*
    id автора объявления
     */
    private Integer author;

    /*
    ссылка на картинку объявления
     */
    private String image;
    /*
    id объявления
     */
    private Integer pk;
    /*
    цена объявления
     */
    private Integer price;
    /*
    заголовок объявления
     */
    private String title;
}
