package ru.skypro.homework.dto.ads;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdResponseDto {
    private long pk;

    private String authorFirstName;

    private String authorLastName;

    private String description;

    private String email;

    private String image;

    private String phone;

    private BigDecimal price;

    private String title;
}
