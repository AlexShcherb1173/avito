package ru.skypro.homework.dto.ads;

import lombok.Data;

@Data
public class AdDto {
    int author;
    Integer pk;     //id объявления
    Integer price;
    String title;   //заголовок объявления
    String image;   //ссылка на картинку объявления
}
