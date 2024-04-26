package ru.skypro.homework.dto.rq.comment;

import lombok.Data;

@Data
public class CreateOrUpdateComment {
    /**
     * Поля относящиеся к сущности CommentEntity
     */
    private String text;
}
