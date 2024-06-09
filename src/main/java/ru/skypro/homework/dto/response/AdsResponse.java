package ru.skypro.homework.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AdsResponse {
    /**
     * общее количество объявлений
     */
    @Schema(description = "общее количество объявлений")
    private Integer count;
    /**
     * {@link AdResponse}
     */
    private List<AdResponse> results;
}
