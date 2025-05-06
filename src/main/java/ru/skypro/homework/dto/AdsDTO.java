package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(name = "Ads")
@Data
public class AdsDTO {
    private Integer count;
    private List<AdDTO> results;

    public AdsDTO() {
    }

    public AdsDTO(List<AdDTO> results) {
        this.results = results;
        count = results.size();
    }
}
