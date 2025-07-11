package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Список комментариев с пагинацией")
public class Comments {
    private Integer count;
    private List<Comment> results;
}