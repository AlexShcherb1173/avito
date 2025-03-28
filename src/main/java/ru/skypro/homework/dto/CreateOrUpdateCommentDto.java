package ru.skypro.homework.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateOrUpdateCommentDto {
    @NotBlank
    @Size(min = 8, max = 64)
    private String text;
}
