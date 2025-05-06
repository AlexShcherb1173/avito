package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UpdateUser")
@Data
public class UpdateUserDTO {
    @Size(min = 2, max = 16, message = "Длина имени должна быть от 2 до 16 символов")
    private String firstName;

    @Size(min = 2, max = 16, message = "Длина фамилии должна быть от 2 до 16 символов")
    private String lastName;

    @Pattern(regexp = "^\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}$",
            message = "Номер телефона должен соответствовать формату +7 XXX XXX-XX-XX")
    private String phone;
}
