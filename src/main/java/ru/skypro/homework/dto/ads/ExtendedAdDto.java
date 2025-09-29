package ru.skypro.homework.dto.ads;

import lombok.Data;

@Data
public class ExtendedAdDto {
    private int pk;     //id объявления
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;       //логин автора объявления
    private String image;       //ссылка на картинку объявления
    private String phone;
    private int price;
    private String title;
}
