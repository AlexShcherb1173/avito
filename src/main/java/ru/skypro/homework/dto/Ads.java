package ru.skypro.homework.dto;

import lombok.*;

import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor // создает конструктор
@NoArgsConstructor // создает пустой конструктор
// список объявлений с пагинацией
public class Ads {
    private Integer count = 0;      // Общее количество объявлений
    private List<Ad> results = List.of(); // Список объявлений (по умолчанию пустой)
}
