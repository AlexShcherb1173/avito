package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Класс, представляющий DTO для сущности "Объявление".
 * Используется для передачи данных об объявлении между слоями приложения.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdDto {

    /**
     * ID автора объявления.
     * Поле обязательно для заполнения.
     */
    @Schema(type = "integer", format = "int32", description = "id автора объявления")
    @NotNull(message = "ID автора обязательно")
    private Integer author;

    /**
     * Ссылка на изображение объявления.
     * Поле обязательно для заполнения.
     */
    @Schema(type = "string", description = "ссылка на картинку объявления")
    @NotBlank(message = "Ссылка на изображение обязательна")
    private String image;

    /**
     * ID объявления.
     * Поле обязательно для заполнения.
     */
    @Schema(type = "integer", format = "int32", description = "id объявления")
    @NotNull(message = "ID объявления обязательно")
    private Integer id;

    /**
     * Цена объявления.
     * Поле обязательно для заполнения и не может быть отрицательным.
     */
    @Schema(type = "integer", format = "int32", description = "цена объявления")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    @NotNull(message = "Цена обязательна")
    private String price;

    /**
     * Заголовок объявления.
     * Поле обязательно для заполнения и не может быть длиннее 255 символов.
     */
    @Schema(type = "string", description = "заголовок объявления")
    @Size(max = 255, message = "Заголовок не может быть длиннее 255 символов")
    @NotBlank(message = "Заголовок обязателен")
    private String title;
}