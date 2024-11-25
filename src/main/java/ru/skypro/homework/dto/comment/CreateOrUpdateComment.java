package ru.skypro.homework.dto.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <b>Класс представляет собой DTO (Data Transfer Object) для создания или обновления комментария. </b> <p>
 *
 * <p>Содержит текст комментария, который может быть использован как при создании нового комментария,
 * так и при обновлении существующего.</p>
 *
 * <p>Используется в слоях приложения, связанных с обработкой комментариев.</p>
 */
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateComment {

    /**
     * <b>Текст комментария. </b> <p>
     *
     * <p>Должен содержать содержимое комментария, которое будет отображаться пользователям.</p>
     */

    @NotBlank
    @Size(min = 8)
    private String text;

}
