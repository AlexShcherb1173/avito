package ru.skypro.homework.dto.ad;

import lombok.Data;

/**
 * DTO для создания или обновления объявления
 */
@Data
public class CreateAndUpdateAd {
    private String title;
    private Integer price;
    private String description;

}
