package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    @Size(min = 3, max = 10, message = "Имя должно иметь от 3 до 10 символов")
    @Schema(description = "Имя пользователя")
    private String firstName;

    @Size(min = 3, max = 10, message = "Фамилия должна содержать от 3 до 10 символов")
    @Schema(description = "Фамилия пользователя")
    private String lastName;

    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", message = "Телефон должен соответствовать формату +7 (ххх) ххх-хх-хх")
    @Schema(description = "Телефонный номер пользователя")
    private String phone;

}
