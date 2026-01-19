package ru.skypro.homework.dto;

import javax.validation.constraints.*;

public class CreateOrUpdateAd {

    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 3, max = 200, message = "Заголовок должен содержать от 3 до 200 символов")
    private String title;

    @NotNull(message = "Цена обязательна")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private Integer price;

    private String description;

    public CreateOrUpdateAd() {}

    public CreateOrUpdateAd(String title, Integer price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public Integer getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}