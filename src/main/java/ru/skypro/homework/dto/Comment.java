package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Comment {
    @JsonProperty("author")
    private long author;	// id автора комментария
    @JsonProperty("authorImage")
    private String authorImage;	// ссылка на аватар автора комментария
    @JsonProperty("authorFirstName")
    private String authorFirstName;	// имя создателя комментария
    @JsonProperty("createdAt")
    private long createdAt;	// дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
    @JsonProperty("pk")
    private long pk;	// id комментария
    @JsonProperty("text")
    private String text;	// текст комментария
}
