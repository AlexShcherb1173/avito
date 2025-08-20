package ru.skypro.homework.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "DTO for advertisements with pagination")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ads {
    @Schema(description = "Total number of advertisements", example = "100")
    int count;
    @Schema(description = "List of advertisements", example = "[{\"id\": 1, \"title\": \"Ad 1\", \"price\": 1000}, {\"id\": 2, \"title\": \"Ad 2\", \"price\": 2000}]")
    List<Ad> results;
}
