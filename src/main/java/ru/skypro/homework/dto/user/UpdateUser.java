package ru.skypro.homework.dto.user;

import lombok.*;
import ru.skypro.homework.validation.Regex;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUser {

    @NotBlank
    @Size(min = 2,max = 16)
    private String firstName;

    @NotBlank
    @Size(min = 2,max = 16)
    private String lastName;

    @Pattern(regexp = Regex.PHONE_REGEXP)
    private String phone;

}
