package ru.skypro.homework.dto.ad;

import lombok.Data;

/**
 * DTO для предоставления расширенного объявления
 */
@Data
public class ExtendedAd {
    private Integer pk;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private Integer price;
    private String title;
}
