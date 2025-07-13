package ru.skypro.homework.dto.commentDTO;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO для создания нового комментария
 */
public class CommentCreateDTO {
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text; // Текст комментария

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
