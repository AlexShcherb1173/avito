package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CreateOrUpdateAd {
    //                              Объявление создаваемое/корректируемое (DTO)
    private String title;       //  Заголовок объявления
    //                              minLength 4
    //                              maxLength 32
    private int price;          //  Цена объявления
    //                              min 0
    //                              max 10000000
    private String description; //  Описание объявления
    //                              minLength 8
    //                              maxLength 64

}
