package ru.skypro.homework.dto.ads;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

public class CreateOrUpdateAd {
    @NotBlank
    @Size(min = 4, max = 32)
    private String title;

    @NotNull
    @Min(0)
    @Max(10_000_000)
    private Integer price;

    @NotBlank
    @Size(min = 8, max = 64)
    private String description;
}