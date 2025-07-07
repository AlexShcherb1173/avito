package ru.skypro.homework.dto.Advertisement;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
@Schema(description = "Список объявлений с пагинацией")
public class Ads {

    @Schema(description = "Общее количество объявлений")
    private Integer count ;

    @Schema(description = "Список объявлений")
    private List<AdDTO> results ;

    public Ads() {}

    public Ads(Integer count, List<AdDTO> results) {
        this.count = count;
        this.results = results;
    }
}
