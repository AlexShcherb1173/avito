package ru.skypro.homework.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotBlank
    @Size(min = 4, max = 32)
    private String username;
    @NotBlank
    @Size(min = 8, max = 16)
    private String password;
}
