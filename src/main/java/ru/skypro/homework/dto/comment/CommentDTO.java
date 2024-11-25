package ru.skypro.homework.dto.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>Класс CommentDTO представляет собой объект передачи данных (DTO),
 * который используется для передачи информации о комментарии. </b> <p>
 *
 * <p>Содержит информацию об авторе комментария, времени создания,
 * идентификаторе комментария и тексте самого комментария.</p>
 *
 * <p>Использует аннотации Lombok для автоматической генерации
 * геттеров, сеттеров, конструкторов и других методов.</p>
 */
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    /**
     * <b>Идентификатор автора комментария. </b> <p>
     */
    private long author;

    /**
     * <b>URL изображения автора комментария. </b> <p>
     */
    private String authorImage;

    /**
     * <b>Имя автора комментария. </b> <p>
     */
    private String authorFirstName;

    /**
     * <b>Дата и время создания комментария. </b> <p>
     */
    private LocalDateTime createdAt;

    /**
     * <b>Уникальный идентификатор комментария. </b> <p>
     */
    private long pk;

    /**
     * <b>Текст комментария. </b> <p>
     */

    @NotBlank
    @Size(min = 8)
    private String text;

}
