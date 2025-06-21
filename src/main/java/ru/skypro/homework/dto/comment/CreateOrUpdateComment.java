package ru.skypro.homework.dto.comment;

import lombok.Data;

/**
 * DTO для создания или обновления комментария
 */
@Data
public class CreateOrUpdateComment {
    private String text;
}
