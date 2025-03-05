package ru.skypro.homework.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;

@Data
public class ExtendedAdDTO {

    @Schema(description = "ID объявления")
    private int pk;

    @Schema(description = "Имя автора объявления")
    @Size(min = 2, max = 32, message = "Имя автора должно содержать от 2 до 32 символов")
    private String authorFirstName;

    @Schema(description = "Фамилия автора объявления")
    @Size(min = 2, max = 32, message = "Фамилия автора должна содержать от 2 до 32 символов")
    private String authorLastName;

    @Schema(description = "Описание объявления")
    @Size(min = 8, max = 256, message = "Описание должно содержать от 8 до 256 символов")
    private String description;

    @Schema(description = "Логин автора объявления (email)")
    @Email(message = "Некорректный формат email")
    private String email;

    @Schema(description = "Ссылка на картинку объявления")
    private String image;

    @Schema(description = "Телефон автора объявления", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;

    @Schema(description = "Цена объявления")
    private int price;

    @Schema(description = "Заголовок объявления")
    @Size(min = 4, max = 32, message = "Заголовок объявления должен содержать от 4 до 32 символов")
    private String title;
}
