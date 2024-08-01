package ru.skypro.homework.dto.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;
@Data
public class CommentsDTO {
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("results")
    @Valid
    private List<CommentDTO> results;
}
