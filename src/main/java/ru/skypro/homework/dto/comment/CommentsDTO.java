package ru.skypro.homework.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CommentsDTO {

    private Integer count;
    private List<CommentDTO> results;
}
