package ru.skypro.homework.dto;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode

public class CommentsDto {
    private int count;
    private List<CommentDto> results;
}
