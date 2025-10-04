package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object для создания или обновления объявления.
 * Используется при создании нового объявления или редактировании существующего.
 */
@Schema(description = "Create or update ad request")
public class CreateOrUpdateAdDto {
    @Schema(description = "Title")
    private String title;

    @Schema(description = "Price")
    private Integer price;

    @Schema(description = "Description")
    private String description;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}