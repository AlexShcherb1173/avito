package ru.skypro.homework.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class NewPasswordDto {
    @NotBlank
    @Size(min = 8, max = 16)
    private String newPassword;
    @NotBlank
    @Size(min = 8, max = 16)
    private String currentPassword;

}
