package ru.skypro.homework.dto.ads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExtendedAdDto {
    // расширенное представление объявления

    @JsonProperty("pk") // id объявления
    private Integer pk;

    @JsonProperty("authorFirstName") // имя автора объявления
    private String authorFirstName;

    @JsonProperty("authorLastName") // фамилия автора объявления
    private String authorLastName;

    @JsonProperty("description") // описание объявления
    private String description;

    @JsonProperty("email") // email автора объявления
    private String email;

    @JsonProperty("image") // ссылка на картинку объявления
    private String image;

    @JsonProperty("phone") // телефон автора объявления
    private String phone;

    @JsonProperty("price") // цена объявления
    private Integer price;

    @JsonProperty("title") // заголовок объявления
    private String title;
}
