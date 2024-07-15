package ru.skypro.homework.dto.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;
@Data
public class CommentsDto {
    // Dto для списка комментариев
    // где results - коллекция комментариев в формате AdDto

    @JsonProperty("count")
    private Integer count;
    @JsonProperty("results")
    @Valid
    private List<CommentDto> results;

}
