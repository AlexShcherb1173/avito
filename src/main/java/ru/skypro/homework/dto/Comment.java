package example.src.main.java.ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "Комментарий")
public class Comment {
    @Schema(description = "id автора комментария")
    private int author;


    @Schema(description = "ссылка на аватар автора комментария")
    private String authorImage;


    @Schema(description = "имя создателя комментария")
    private String authorFirstName;


    @Schema(description = "дата и время создания комментария")
    private long createdAt;


    @Schema(description = "id комментария")
    private int pk;


    @Schema(description = "текст комментария")
    private String text;
}