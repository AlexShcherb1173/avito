package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object для создания или обновления комментария.
 * Используется при добавлении нового комментария или редактировании существующего.
 */
@Schema(description = "Create or update comment request")
public class CreateOrUpdateCommentDto {
    @Schema(description = "Text")
    private String text;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}