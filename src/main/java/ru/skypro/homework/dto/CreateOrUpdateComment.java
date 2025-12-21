package ru.skypro.homework.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor // создает конструктор
@NoArgsConstructor // создает пустой конструктор
// создание/обновление комментария:
public class CreateOrUpdateComment {
        private String text ; // Текст комментария
}

