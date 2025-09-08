package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
@Data
@Schema(description = "Ответ со списком комментариев")
public class Comments {

    @Schema(description = "Общее количество комментариев", example = "3", required = true)
    private Integer count;

    @Schema(description = "Список комментариев")
    private List<Comment> results;
}
