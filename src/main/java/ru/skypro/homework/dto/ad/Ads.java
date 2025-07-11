package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Список объявлений с пагинацией")
public class Ads {
    private Integer count;
    private List<Ad> results;
}