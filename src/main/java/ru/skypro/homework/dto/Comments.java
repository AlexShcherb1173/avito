package ru.skypro.homework.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель данных нескольких комментариев")
public class Comments {
    @Schema(
            type = "integer",
            format = "int32",
            description = "общее количество комментариев"
    )
    private Integer count;

    @Schema(
            type = "array"
    )
    private Collection<Comment> results = new ArrayList<>();
}
