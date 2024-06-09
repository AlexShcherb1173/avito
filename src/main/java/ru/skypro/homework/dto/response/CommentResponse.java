package ru.skypro.homework.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentResponse {
    /**
     * id автора комментария
     */
    @Schema(description = "id автора комментария")
    private Integer author;
    /**
     * ссылка на аватар автора комментария
     */
    @Schema(description = "ссылка на аватар автора комментария")
    private String authorImage;
    /**
     * имя создателя комментария
     */
    @Schema(description = "имя создателя комментария")
    private String authorFirstName;
    /**
     * дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
     */
    @Schema(description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
    private Integer createdAt;
    /**
     * id комментария
     */
    @Schema(description = "id комментария")
    private Integer pk;
    /**
     * текст комментария
     */
    @Schema(description = "текст комментария")
    private String text;
}
