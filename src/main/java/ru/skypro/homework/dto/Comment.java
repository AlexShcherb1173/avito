package ru.skypro.homework.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Информация о комментарии
 */
@Schema(description = "Информация о комментарии к объявлению")
@Data
public class Comment {

    @Schema(description = "ID автора комментария")
    private Integer author = 0;

    @Schema(description = "Ссылка на аватар автора")
    private String authorImage = "";

    @Schema(description = "Имя автора")
    private String authorFirstName = "";

    @Schema(description = "Дата и время создания")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Schema(description = "ID комментария")
    private Integer pk = 0;

    @Schema(description = "Текст комментария")
    private String text = "";
}
