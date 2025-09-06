package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Комментарий")
public class Comment {
    @Schema(description = "id автора комментария")
    private Integer author;
    @Schema(description = "ссылка на аватар автора комментария")
    private String authorImage;
    @Schema(description = "имя создателя комментария")
    private String authorFirstName;
    @Schema(description = "дата/время создания, мс с 01.01.1970")
    private Long createdAt;
    @Schema(description = "id комментария")
    private Integer pk;
    @Schema(description = "текст комментария")
    private String text;
}
