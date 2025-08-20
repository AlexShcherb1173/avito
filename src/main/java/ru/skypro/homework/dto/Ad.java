package ru.skypro.homework.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ad entity representing an advertisement in the system")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ad {
    @Schema(description = "Unique identifier Author", example = "1")
    int author;
    @Schema(description = "Author's image URL")
    String image;
    @Schema(description = "Ad's id", example = "2")
    int pk;
    @Schema(description = "Price of product", example = "200")
    int price;
    @Schema(description = "Title of the ad", example = "Chair for sale")
    String title;
}
