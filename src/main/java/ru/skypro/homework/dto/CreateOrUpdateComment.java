package ru.skypro.homework.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель данных для создания или обновления комментария")
public class CreateOrUpdateComment {
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
    type = "string",
    description = "текст комментария")
    @Size(min = 8, max = 64)
    private String text;
}
