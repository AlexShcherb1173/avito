package ru.skypro.homework.dto.ads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.validation.Valid;
import java.util.List;

@Data
public class AdsDto {

    // Dto для списка объявлений
    // где results - коллекция объявлений в формате AdDto
    @JsonProperty("count") // общее количество объявлений
    private Integer count;

    @JsonProperty("results") // коллекция объявлений
    @Valid
    private List<AdDto> results;

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setResults(List<AdDto> results) {
        this.results = results;
    }
}
