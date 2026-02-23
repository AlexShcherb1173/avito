package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Комментарий")
public class Comment {

    @Schema(description = "id автора комментария", example = "1")
    private Integer author;

    @Schema(description = "аватар автора", example = "/images/avatar.jpg")
    private String authorImage;

    @Schema(description = "имя автора", example = "Иван")
    private String authorFirstName;

    @Schema(description = "дата создания в миллисекундах", example = "1670000000000")
    private Long createdAt;

    @Schema(description = "id комментария", example = "100")
    private Integer pk;

    @Schema(description = "текст комментария", example = "Отличное объявление")
    private String text;
}