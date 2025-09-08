package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Комментарий к объявлению")
public class Comment {

    @Schema(description = "Идентификатор автора комментария", example = "42", required = true)
    private Integer author;

    @Schema(description = "URL аватарки автора", example = "/images/users/42.png")
    private String authorImage;

    @Schema(description = "Имя автора", example = "Иван")
    private String authorFirstName;

    @Schema(description = "Метка времени создания (epoch millis)", example = "1716405600000", required = true)
    private Long createdAt;

    @Schema(description = "Идентификатор комментария", example = "555", required = true)
    private Integer pk;

    @Schema(description = "Текст комментария", example = "Отличное объявление!", required = true)
    private String text;
}
