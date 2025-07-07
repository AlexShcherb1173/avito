package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExtendedAd {
    @JsonProperty("pk")
    private long pk;	// id объявления
    @JsonProperty("authorFirstName")
    private String authorFirstName;	// имя автора объявления
    @JsonProperty("authorLastName")
    private String authorLastName;	// фамилия автора объявления
    @JsonProperty("description")
    private String description;	// описание объявления
    @JsonProperty("email")
    private String email;	// логин автора объявления
    @JsonProperty("image")
    private String image;	// ссылка на картинку объявления
    @JsonProperty("phone")
    private String phone;	// телефон автора объявления
    @JsonProperty("price")
    private int price;	// цена объявления
    @JsonProperty("title")
    private String title;	// заголовок объявления
}
