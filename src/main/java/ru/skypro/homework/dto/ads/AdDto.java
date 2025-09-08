package ru.skypro.homework.dto.ads;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdDto {
    private long author;

    private String image;

    private long pk;

    private BigDecimal price;

    private String title;
}
