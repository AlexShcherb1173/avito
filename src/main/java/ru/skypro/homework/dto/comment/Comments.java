package ru.skypro.homework.dto.comment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

public class Comments {

    private Integer count;

    private List<CommentDTO> results;
}
