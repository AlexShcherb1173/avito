package ru.skypro.homework.dto.comments;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateCommentDto {
    @NotBlank(message = "Текст комментария не может быть пустым")
    @Size(min = 8, max = 64, message = "Текст комментария должен быть от 8 до 64 символов")
    private String text;
}
