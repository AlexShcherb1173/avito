package ru.skypro.homework.filter.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO для фильтрации")
public class FilterDto {
    @Schema(description = "Название фильтра", example = "price")
    private String key;

    @Schema(description = "Значение фильтра", example = "1000")
    private String value;

    // геттеры и сеттеры
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}