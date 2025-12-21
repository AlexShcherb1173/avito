package ru.skypro.homework.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor // создает конструктор
@NoArgsConstructor // создает пустой конструктор
//базовая информация об объявлении
public class Ad {
    private Integer author; // ID автора
    private String image ;  // ссылка на картинку объявления
    private Integer pk ;     // ID объявления
    private Integer price;  // Цена объявления
    private String title ;  // Заголовок объявления
}
