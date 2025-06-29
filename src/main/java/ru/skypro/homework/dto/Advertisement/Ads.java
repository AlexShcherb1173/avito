package ru.skypro.homework.dto.Advertisement;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * Список объявлений с пагинацией
 */
@Data
@Schema(description = "Список объявлений с пагинацией")
public class Ads {

    @Schema(description = "Общее количество объявлений")
    private Integer count = 0;

    @Schema(description = "Список объявлений")
    private List<AdDTO> results = Collections.emptyList();
}
