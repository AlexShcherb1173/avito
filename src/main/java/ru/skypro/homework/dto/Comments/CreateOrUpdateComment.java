package ru.skypro.homework.dto.Comments;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Данные для создания или обновления комментария")
public class CreateOrUpdateComment {

        @NotBlank
        @Schema(description = "Текст комментария",
                minLength = 8,
                maxLength = 64)
        private String text = "";

}

