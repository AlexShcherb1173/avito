package ru.skypro.homework.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor // создает конструктор
@NoArgsConstructor // создает пустой конструктор
//    информация о комментарии:
public class Comment {
    private Integer author;         // id автора комментария
    private String authorImage;    // ссылка на аватар автора комментария
    private String authorFirstName;// имя создателя комментария
    private Long createdAt;        // Дата создания (в миллисекундах)
    private Integer pk;             // ID комментария
    private String text;           // Текст комментария
}
