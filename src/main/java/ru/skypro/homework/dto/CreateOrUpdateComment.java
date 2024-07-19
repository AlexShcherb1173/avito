package ru.skypro.homework.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CreateOrUpdateComment {

    @NotNull
    @Schema(description = "текст комментария",
            required = true,
            minLength = 8,
            maxLength = 64)
    private String text;
}
