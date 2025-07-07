package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Ad {
    @JsonProperty("author")
    private long author;	// id автора объявления

    @JsonProperty("image")
    private String image;	// ссылка на картинку объявления

    @JsonProperty("pk")
    private long pk;	// id объявления

    @JsonProperty("price")
    private int price;	// цена объявления

    @JsonProperty("title")
    private String title;	// заголовок объявления
}
