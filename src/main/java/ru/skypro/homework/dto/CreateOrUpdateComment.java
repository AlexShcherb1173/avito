package ru.skypro.homework.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "DTO для создания/обновления комментария")
@Data
public class CreateOrUpdateComment {

    @Schema(description = "текст комментария (min 8, max 64)")
    private String text;

}
