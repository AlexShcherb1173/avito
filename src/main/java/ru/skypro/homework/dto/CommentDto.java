package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель данных комментария")
public class CommentDto {
    @Schema(
            type = "integer",
            format = "int32",
            description = "id автора комментария"
    )
    private Integer author;

    @Schema(
            type = "string",
            description = "ссылка на аватар автора комментария"
    )
    private String authorImage;

    @Schema(
            type = "string",
            description = "имя создателя комментария"
    )
    private String authorFirstName;

    @Schema(
            type = "integer",
            format = "int64",
            description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970"
    )
    private Long createdAt;

    @Schema(
            type = "integer",
            format = "int32",
            description = "id комментария"
    )
    private Integer pk;

    @Schema(
            type = "string",
            description = "текст комментария"
    )
    private String text;
}
