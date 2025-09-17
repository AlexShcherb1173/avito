package ru.skypro.homework.dto.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Комментарий к объявлению")
public class CommentDto {
    @JsonProperty("author")
    @Schema(description = "Идентификатор автора")
    private long idAuthor;
    @Schema(description = "Ссылка на изображение автора")
    private String authorImage;
    @Schema(description = "Имя автора")
    private String authorFirstName;
    @Schema(description = "Дата создания")
    private LocalDateTime createdAt;
    @JsonProperty("pk")
    @Schema(description = "Идентификатор комментария")
    private long id;
    @Schema(description = "Текст комментария")
    private String text;
}
