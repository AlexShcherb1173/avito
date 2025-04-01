package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Ads")
@Data
public class AdsDTO {
    private Integer count;
    private List<AdDTO> results;

    public AdsDTO(List<AdDTO> results) {
        this.results = results;
        count = results.size();
    }
}
