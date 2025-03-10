package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Класс, представляющий собой Data Transfer Object (DTO) для объявления.
 * Используется для передачи данных объявления в различных слоях приложения.
 * Включает информацию о авторе объявления, изображении, идентификаторе объявления, цене, заголовке и описании.
 * Все поля содержат соответствующие аннотации для валидации данных и их описания в Swagger.
 */
@Data
public class AdDTO {

    @Schema(description = "ID автора объявления")
    @NotNull(message = "ID автора не должен быть пустым")
    private int author;

    @Schema(description = "Ссылка на картинку объявления")
    private String image;

    @Schema(description = "ID объявления")
    @NotNull(message = "ID объявления не должен быть пустым")
    private int pk;

    @Schema(description = "Цена объявления")
    @NotNull(message = "Цена не должна быть пустой")
    private Integer price;

    @Schema(description = "Заголовок объявления")
    private String title;

    @Schema(description = "Описание объявления", minLength = 8, maxLength = 64)
    @Size(min = 8, max = 64, message = "Описание должно содержать от 8 до 64 символов")
    @NotBlank(message = "Описание не должно быть пустым")
    private String description;
}
