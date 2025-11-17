package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель данных нескольких рекламных объявлений")
public class AdsDto {
    @Schema(
            type = "integer",
            format = "int32",
            description = "общее количество объявлений"
    )
    private Integer count;

    @Schema(
            type = "array"
    )
    private Collection<AdDto> results = new ArrayList<>();
}
