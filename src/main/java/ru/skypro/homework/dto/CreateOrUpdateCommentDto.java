package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.skypro.homework.service.impl.CheckService;

import java.util.Objects;

/**
 * DTO (Data Transfer Object) для создания или обновления комментария.
 * <p>
 * В классе представлены данные для создания или обновления комментария, а именно: текст комментария.
 * </p>
 */
public class CreateOrUpdateCommentDto {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            description = "текст комментария",
            minLength = 8,
            maxLength = 64)
    private String text;

    @Override
    public String toString() {
        return "CreateOrUpdateComment{" +
                "text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateOrUpdateCommentDto that = (CreateOrUpdateCommentDto) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (CheckService.checkLength(text, 8, 64)) {
            this.text = text;
        } else {
            throw new IllegalArgumentException("Длина комментария от 8 до 64 символов");
        }
    }

    public CreateOrUpdateCommentDto() {
    }

    public CreateOrUpdateCommentDto(String text) {
        if (CheckService.checkLength(text, 8, 64)) {
            this.text = text;
        } else {
            throw new IllegalArgumentException("Длина комментария от 8 до 64 символов");
        }
    }
}
