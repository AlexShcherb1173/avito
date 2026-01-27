package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@ApiModel(value = "Comments", description = "Модель списка комментариев")
public class CommentsDto {

    @PositiveOrZero(message = "Количество комментариев не может быть отрицательным")
    @ApiModelProperty(
            value = "Общее количество комментариев",
            example = OpenApiConstant.COUNT_COMMENTS)
    private Integer count;

    @Size(min = 0, message = "Список комментариев не может быть отрицательным")
    @ApiModelProperty(
            value = "Список комментариев",
            example = OpenApiConstant.LIST_COMMENTS)
    private List<CommentDto> results;

}
