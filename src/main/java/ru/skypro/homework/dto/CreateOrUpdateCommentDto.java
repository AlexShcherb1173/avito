package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateCommentDto {
    @Schema(description = "общее количество комментариев")
    //@Size(min = 8, max = 64)
    private String text;
}
