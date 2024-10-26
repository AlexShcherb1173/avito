package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class ExtendedAd {
    //                                      Объявление расширенное (DTO)
    private int pk;                     //  id объявления
    private String authorFirstName;     //  Имя автора объявления
    private String authorLastName;      //  Фамилия автора объявления
    private String description;         //  Описание объявления
    private String email;               //  Логин автора объявления
    private String image;               //  Ссылка на картинку объявления
    private String phone;               //  Телефон автора объявления
    private int price;                  //  Цена объявления
    private String title;               //  Заголовок объявления

}
