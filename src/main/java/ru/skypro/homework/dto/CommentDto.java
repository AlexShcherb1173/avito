package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Класс, представляющий DTO для сущности "Комментарий".
 * Используется для передачи данных о комментарии между слоями приложения.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    /**
     * ID автора комментария.
     * Поле обязательно для заполнения.
     */
    @Schema(type = "integer", format = "int32", description = "id автора комментария")
    @NotNull(message = "ID автора обязательно")
    private Integer author;

    /**
     * Ссылка на аватар автора комментария.
     */
    @Schema(type = "string", description = "ссылка на аватар автора комментария")
    private String authorImage;

    /**
     * Имя автора комментария.
     */
    @Schema(type = "string", description = "имя создателя комментария")
    private String authorFirstName;

    /**
     * Дата и время создания комментария в миллисекундах (Unix-время).
     */
    @Schema(type = "integer", format = "int64", description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
    private Long createdAt;

    /**
     * ID комментария.
     * Поле обязательно для заполнения.
     */
    @Schema(type = "integer", format = "int32", description = "id комментария")
    @NotNull(message = "ID комментария обязательно")
    private Integer pk;

    /**
     * Текст комментария.
     * Поле обязательно для заполнения.
     */
    @Schema(type = "string", description = "текст комментария")
    @NotBlank(message = "Текст комментария обязателен")
    private String text;
}