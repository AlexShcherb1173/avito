package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Data
@Getter
@Setter
public class CommentsResponseDTO {
    private Integer count;
    private List<CommentResponseDTO> results = Collections.singletonList(new CommentResponseDTO());
}
