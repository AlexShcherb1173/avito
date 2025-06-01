package ru.skypro.homework.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class CreateOrUpdateAd {

    private String title; // Заголовок объявления
    private String price; // Цена объявления
    private String description; // Описание объявления

    public CreateOrUpdateAd(String title, String price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CreateOrUpdateAd that = (CreateOrUpdateAd) o;
        return Objects.equals(title, that.title) && Objects.equals(price, that.price) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, price, description);
    }

    @Override
    public String toString() {
        return "CreateOrUpdateAd{" +
                "title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
