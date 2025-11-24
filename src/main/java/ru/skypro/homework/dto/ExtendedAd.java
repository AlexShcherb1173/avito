package ru.skypro.homework.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor // создает конструктор
@NoArgsConstructor // создает пустой конструктор
//расширенная информация об объявлении:
public class ExtendedAd {
    private Integer pk ;              // ID объявления
    private String authorFirstName; // Имя автора
    private String authorLastName ;  // Фамилия автора
    private String description ;     // Описание
    private String email ;           // Email автора
    private String image ;           // Ссылка на картинку
    private String phone ;           // Телефон автора
    private Integer price ;           // Цена
    private String title ;           // Заголовок
}
