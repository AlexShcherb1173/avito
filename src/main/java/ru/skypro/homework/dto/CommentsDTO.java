package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(name = "Comments")
@Data
public class CommentsDTO {
    private Integer count;
    private List<CommentDTO> results;

    public CommentsDTO() {
    }

    public CommentsDTO(Integer count, List<CommentDTO> commentDTOS) {
    }
}
