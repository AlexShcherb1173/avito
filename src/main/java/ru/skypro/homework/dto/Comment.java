package ru.skypro.homework.dto;

import lombok.Data;


@Data
public class Comment {
    /*
   id автора комментария
    */
    private Integer author;
    /*
   ссылка на аватар автора комментария
    */
    private String authorImage;
    /*
   имя создателя комментария
    */
    private String authorFirstName;
    /*
    дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
     */
    private Long createdAt;
    /*
   id комментария
    */
    private Integer pk;
    /*
   текст комментария
    */
    private String text;
}
