package ru.skypro.homework.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateOrUpdateCommentDto {

    @NotNull
    @Schema(description = "текст комментария",
            minLength = 8,
            maxLength = 64)
    private String text;
}
