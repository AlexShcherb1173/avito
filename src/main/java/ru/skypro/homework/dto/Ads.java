package ru.skypro.homework.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "DTO со списком объявлений")
@Data
public class Ads {

    @Schema(description = "общее количество объявлений")
    private Integer count;

    @Schema(description = "список объявлений")
    private List<Ad> results;

}
