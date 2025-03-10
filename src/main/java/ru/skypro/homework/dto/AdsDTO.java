package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Класс, представляющий собой Data Transfer Object (DTO) для списка объявлений.
 * Используется для передачи данных о группе объявлений, включая общее количество и сами объявления.
 * Включает поле для общего количества объявлений, списка объявлений и отдельного объявления.
 */
@Data
public class AdsDTO {

    @Schema(description = "Общее количество объявлений")
    private int count;
    private AdDTO ad;

    @Schema(description = "Список объявлений")
    private List<AdDTO> results;

    public AdsDTO(int count, List<AdDTO> results) {
        this.count = count;
        this.results = results;
    }
}
