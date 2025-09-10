package ru.skypro.homework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@Builder
@ApiModel(value = "Ads", description = "Модель списка объявлений")
public class AdsDto {

    @PositiveOrZero(message = "Количество объявлений не может быть отрицательным")
    @ApiModelProperty(value = "Общее количество объявлений", example = OpenApiConstant.COUNT_ADS)
    private Integer count;

    @Size(min = 0, message = "Список объявлений не может быть отрицательным")
    @ApiModelProperty(value = "Список объявлений", example = OpenApiConstant.LIST_ADS)
    private List<AdDto> results;


}
