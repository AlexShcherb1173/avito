package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.skypro.homework.service.impl.CheckService;

import java.util.Objects;
/**
 * DTO (Data Transfer Object) для создания или обновления объявления.
 * <p>
 * В классе представлены данные для создания или обновления объявления, а именно: заголовок, цена, описание.
 * Также в классе реализованы конструкторы, геттеры, сеттеры, equals, hashCode и toString.
 * </p>
 */
public class CreateOrUpdateAdDto {

    @Schema(type = "string",
            description = "заголовок объявления",
            minLength = 4,
            maxLength = 32)
    private String title;

    @Schema(type = "integer",
            format = "int32",
            description = "цена объявления",
            minimum = "0",
            maximum = "10000000")
    private Integer price;

    @Schema(type = "string",
            description = "описание объявления",
            minLength = 8,
            maxLength = 64)
    private String description;

    @Override
    public String toString() {
        return "CreateOrUpdateAd{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateOrUpdateAdDto that = (CreateOrUpdateAdDto) o;
        return Objects.equals(title, that.title) && Objects.equals(price, that.price) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, price, description);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (CheckService.checkLength(title, 4, 32)) {
            this.title = title;
        } else {
            throw new IllegalArgumentException("Длина заголовка от 4 до 32 символов");
        }
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        if (CheckService.checkPrice(price, 0, 10000000)) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Цена в диапазоне от 0 до 10,000,000");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (CheckService.checkLength(description, 8, 64)) {
            this.description = description;
        } else {
            throw new IllegalArgumentException("Длина описания от 8 до 64 символов");
        }
    }

    public CreateOrUpdateAdDto() {
    }

    public CreateOrUpdateAdDto(String title, Integer price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }
}
