package example.src.main.java.ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "Список комментариев")
public class Comments {
    @Schema(description = "общее количество комментариев")
    private int count;

    @Schema(description = "список комментариев")
    private List<Comment> results;
}