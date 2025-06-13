package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Comment {

    private Integer author;         // id автора комментария
    private String authorImage;     // Ссылка на аватар автора комментария
    private String authorFirstName; // Имя создателя комментария
    private Integer createdAt;      // Дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
    private Integer pk;             // id комментария
    private String text;            // Текст комментария

    public Comment() {

    }
}
