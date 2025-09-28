package ru.skypro.homework.dto.ads;

import lombok.Data;

@Data
public class Ad {
    int author;
    String image;
    int pk;     //id объявления
    int price;
    String title;   //заголовок объявления
}
