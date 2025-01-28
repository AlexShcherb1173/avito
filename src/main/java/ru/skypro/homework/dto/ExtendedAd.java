package ru.skypro.homework.dto;
/*
Эта схема описывает объект ExtendedAd, который представляет собой расширенное объявление с дополнительной информацией об авторе.
 */
import lombok.Data;

@Data
public class ExtendedAd {
    /*
    id объявления
     */
    private Integer pk;
    /*
    имя автора объявления
     */
    private String authorFirstName;
    /*
    фамилия автора объявления
     */
    private String authorLastName;
    /*
    описание объявления
     */
    private String description;
    /*
    логин автора объявления
     */
    private String email;
    /*
    ссылка на картинку объявления
     */
    private String image;
    /*
    телефон автора объявления
     */
    private String phone;
    /*
    цена объявления
     */
    private Integer price;
    /*
    заголовок объявления
     */
    private String title;
}
