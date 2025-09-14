package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Список комментариев с количеством")
public class Comments {
    @Schema(description = "общее количество комментариев")
    private Integer count;
    private List<Comment> results;
}
