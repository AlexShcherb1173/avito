package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdsDTO {
    @Schema(description = "общее количество объявлений")
    private int count;
    private List<AdDTO> results;
}
