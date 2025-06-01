package ru.skypro.homework.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class ExtendedAd {

    private Integer pk; // идентификатор объявления
    private String authorFirstName; // имя автора объявления
    private String authorLastName; // фамилия автора объявления
    private String description; // описание товара
    private String email; // электронная почта автора
    private String image; // ссылка на изображение товара
    private String phone; // номер телефона автора
    private Integer price; // цена товара
    private String title; // заголовок объявления

    public ExtendedAd(Integer pk, String authorFirstName, String authorLastName,
                      String description, String email, String image,
                      String phone, Integer price, String title) {
        this.pk = pk;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.description = description;
        this.email = email;
        this.image = image;
        this.phone = phone;
        this.price = price;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExtendedAd that = (ExtendedAd) o;
        return Objects.equals(pk, that.pk) && Objects.equals(authorFirstName, that.authorFirstName) && Objects.equals(authorLastName, that.authorLastName) && Objects.equals(description, that.description) && Objects.equals(email, that.email) && Objects.equals(image, that.image) && Objects.equals(phone, that.phone) && Objects.equals(price, that.price) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk, authorFirstName, authorLastName, description, email, image, phone, price, title);
    }

    @Override
    public String toString() {
        return "ExtendedAd{" +
                "pk=" + pk +
                ", authorFirstName='" + authorFirstName + '\'' +
                ", authorLastName='" + authorLastName + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", phone='" + phone + '\'' +
                ", price=" + price +
                ", title='" + title + '\'' +
                '}';
    }
}
