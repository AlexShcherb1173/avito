package ru.skypro.homework.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class UpdateUserDto {
    @NotBlank
    @Size(min = 2, max = 16)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 16)
    private String lastName;
    @NotBlank
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
}
