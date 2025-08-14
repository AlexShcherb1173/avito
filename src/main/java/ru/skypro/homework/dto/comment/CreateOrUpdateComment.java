package ru.skypro.homework.dto.comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

public class CreateOrUpdateComment {
    @NotBlank
    @Size(min = 8, max = 64)
    private String text;
}