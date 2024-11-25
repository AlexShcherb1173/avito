package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skypro.homework.model.CommentEntity;

@Data
@AllArgsConstructor
public class Comments {

    @Schema(description = "Общее количество комментариев")
    private Integer count;

    private Comment[] results;
}
