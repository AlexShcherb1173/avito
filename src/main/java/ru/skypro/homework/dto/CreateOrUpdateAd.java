package ru.skypro.homework.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor // создает конструктор
@NoArgsConstructor // создает пустой конструктор
//создание/обновление объявления:
public class CreateOrUpdateAd {
    private String title;       // Заголовок объявления
    private Integer price ;       // Цена
    private String description; // Описание
}
