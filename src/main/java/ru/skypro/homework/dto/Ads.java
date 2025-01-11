package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ads {

    @Min(value = 0, message = "Количество объявлений не может быть отрицательным")
    private Integer count;

    @NotNull(message = "Список объявлений обязателен")
    private List<AdDto> results;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdDto {
        private Integer id;
        private String title;
        private String price;
        private String image;
        private Integer author;
    }
}