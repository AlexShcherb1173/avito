package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateOrUpdateAd {
    @JsonProperty("title")
    private String title;	// заголовок объявления
    @JsonProperty("price")
    private int price;	// цена объявления
    @JsonProperty("description")
    private String description;	// описание объявления
}

