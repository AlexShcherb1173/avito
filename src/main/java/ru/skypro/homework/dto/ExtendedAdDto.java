package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedAdDto {

    @NotNull(message = "ID объявления обязательно")
    private Integer pk;

    @NotBlank(message = "Имя автора обязательно")
    private String authorFirstName;

    @NotBlank(message = "Фамилия автора обязательна")
    private String authorLastName;

    @Min(value = 0, message = "Цена не может быть отрицательной")
    private Integer price;

    private String description;

    @NotBlank(message = "Логин автора обязателен")
    private String email;

    @NotBlank(message = "Ссылка на изображение обязательна")
    private String image;

    @NotBlank(message = "Телефон автора обязателен")
    private String phone;

    @NotBlank(message = "Заголовок обязателен")
    private String title;
}