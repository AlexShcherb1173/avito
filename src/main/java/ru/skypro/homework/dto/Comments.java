package ru.skypro.homework.dto;

import lombok.*;

import java.util.List;


@Data
@Getter
@Setter
@AllArgsConstructor // создает конструктор
@NoArgsConstructor // создает пустой конструктор
//список комментариев:
public class Comments {
    private Integer count = 0;          // Общее количество комментариев
    private List<Comment> results ; // Список комментариев
}
