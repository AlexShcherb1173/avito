package ru.skypro.homework.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
/**
 * Данные для создания или обновления комментария
 */
@Schema(description = "Данные для создания или обновления комментария")
public class CreateorUpdateComment {
        /**
         * Текст комментария (обязательное поле)
         */
        @NotBlank
        @Schema(description = "Текст комментария",
                minLength = 8,
                maxLength = 255)
        private String text = "";
}

