package ru.skypro.homework.dto.Comments;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Информация о комментарии
 */
@Schema(description = "Информация о комментарии к объявлению")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    @Schema(description = "ID автора комментария")
    private Long author ;

    @Schema(description = "Ссылка на аватар автора")
    private String authorImage = "";

    @Schema(description = "Имя автора")
    private String authorFirstName = "";

    @Schema(description = "Дата и время создания")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Schema(description = "ID комментария")
    private Long pk ;

    @Schema(description = "Текст комментария")
    private String text = "";


}
