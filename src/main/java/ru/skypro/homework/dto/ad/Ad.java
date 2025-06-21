package ru.skypro.homework.dto.ad;

import lombok.Data;

/**
 * DTO для предоставления объявления
 */

@Data
public class Ad {
    private Integer id;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;
}
