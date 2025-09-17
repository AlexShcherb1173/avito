package ru.skypro.homework.dto.ads;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AdRequestDto {
    private String title;

    private BigDecimal price;

    private String description;
}
