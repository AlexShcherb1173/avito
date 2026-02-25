package ru.skypro.homework.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "DTO с информацией о комментарии")
@Data
public class Comment {

    @Schema(description = "id автора комментария")
    private Integer author;

    @Schema(description = "ссылка на аватар автора комментария")
    private String authorImage;

    @Schema(description = "имя создателя комментария")
    private String authorFirstName;

    @Schema(description = "дата и время создания комментария в миллисекундах")
    private Long createdAt;

    @Schema(description = "id комментария")
    private Integer pk;

    @Schema(description = "текст комментария")
    private String text;

}
