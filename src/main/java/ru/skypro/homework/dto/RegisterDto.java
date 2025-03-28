package ru.skypro.homework.dto;

import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RegisterDto {
    @NotBlank
    @Size(min = 4, max = 32)
    private String username;
    @NotBlank
    @Size(min = 8, max = 16)
    private String password;
    @NotBlank
    @Size(min = 2, max = 16)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 16)
    private String lastName;
    @NotBlank
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
}
