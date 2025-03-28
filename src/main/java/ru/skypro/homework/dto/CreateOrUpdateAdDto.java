package ru.skypro.homework.dto;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CreateOrUpdateAdDto {
    @NotBlank
    @Size(min = 4, max = 32, message = "Введите название")
    private String title;
    @Size(max = 10000000)
    private String price;
    @NotBlank
    @Size(min = 8, max = 64, message = "Описание обязательно!")
    private String description;
}
