package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CommentDTO {

    @Schema(description = "ID автора комментария")
    @NotNull(message = "ID автора не должен быть пустым")
    private int author;

    @Schema(description = "Ссылка на аватар автора комментария")
    private String authorImage;

    @Schema(description = "Имя создателя комментария")
    @Size(min = 2, max = 32, message = "Имя автора должно содержать от 2 до 32 символов")
    private String authorFirstName;

    @Schema(description = "Дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
    @NotNull(message = "Дата и время создания не должны быть пустыми")
    private Long createdAt;

    @Schema(description = "ID комментария")
    @NotNull(message = "ID комментария не должен быть пустым")
    private int pk;

    @Schema(description = "Текст комментария")
    @Size(min = 8, max = 64, message = "Текст комментария должен содержать от 8 до 64 символов")
    private String text;
}
