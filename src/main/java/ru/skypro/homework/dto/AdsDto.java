package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AdsDto {

    @Schema(description = "общее количество объявлений")
    private int count;

    @Schema
    private List<AdDto> results;
}
