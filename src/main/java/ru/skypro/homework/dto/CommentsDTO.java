package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Comments")
@Data
public class CommentsDTO {
    private Integer count;
    private List<CommentDTO> results;

}
