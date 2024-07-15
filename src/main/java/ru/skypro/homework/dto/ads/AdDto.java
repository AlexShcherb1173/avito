package ru.skypro.homework.dto.ads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdDto {

    // Dto для объявления
    @JsonProperty("author") // id автора объявления
    private Integer author;

    @JsonProperty("image") // ссылка на картинку объявления
    private String image;

    @JsonProperty("pk") // id объявления
    private Integer pk;

    @JsonProperty("price") // цена объявления
    private Integer price;

    @JsonProperty("title") // заголовок объявления
    private String title;
}
