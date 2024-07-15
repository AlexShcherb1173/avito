package ru.skypro.homework.dto.ads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@Getter
@Setter
@EqualsAndHashCode
public class CreateOrUpdateAdDto {
    @JsonProperty("description") // описание объявления
    @Size(min = 8, max = 64)
    private String description;

    @JsonProperty("title") // заголовок объявления
    @Size(min = 4, max = 32)
    private String title;

    @JsonProperty("price") // цена объявления
    @Min(0)
    @Max(10000000)
    private Integer price;
}
