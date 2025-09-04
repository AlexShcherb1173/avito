package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Ответ со списком объявлений")
public class Ads {

    @Schema(description = "Общее количество объявлений", example = "2", required = true)
    private Integer count;

    @Schema(description = "Список объявлений")
    private List<Ad> results;
}


