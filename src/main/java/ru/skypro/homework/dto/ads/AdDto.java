package ru.skypro.homework.dto.ads;

import lombok.Data;

@Data
public class AdDto {
    int author;
    String image;   //ссылка на картинку объявления
    Integer pk;     //id объявления
    Integer price;
    String title;   //заголовок объявления
}
