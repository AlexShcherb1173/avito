package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Create or update comment request")
public class CreateOrUpdateCommentDto {
    @Schema(description = "Text")
    private String text;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
