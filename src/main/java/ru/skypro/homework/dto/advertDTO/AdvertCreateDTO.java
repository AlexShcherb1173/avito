package ru.skypro.homework.dto.advertDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO для создания/обновления объявления
 */
public class AdvertCreateDTO {
    @NotBlank(message = "Заголовок не может быть пустым")
    private String title; // Заголовок объявления

    @NotBlank(message = "Описание не может быть пустым")
    private String description; // Текст объявления

    @NotNull(message = "Цена должна быть указана")
    @Positive(message = "Цена должна быть положительной")
    private Double price; // Устанавливаемая цена

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
