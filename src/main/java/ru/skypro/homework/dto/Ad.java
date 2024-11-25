package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Ad {

    @Schema(description = "id автора объявления")
    private Integer author;

    @Schema(description = "Ссылка на картинку объявления")
    private String image;

    @Schema(description = "id объявления")
    @Id
    private Integer id;

    @Schema(description = "Цена объявления")
    private Integer price;

    @Schema(description = "Заголовок объявления")
    private String title;
}
