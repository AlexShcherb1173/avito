package ru.skypro.homework.dto.ads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.validation.Valid;
import java.util.List;

@Data
public class AdsDto {
    @JsonProperty("count") // общее количество объявлений
    private Integer count;

    @JsonProperty("results") // коллекция объявлений
    @Valid
    private List<AdDto> results;
}
