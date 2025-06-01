package ru.skypro.homework.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class Ad {

    private Integer author;  // id автора объявления
    private String image; // Ссылка на картинку объявления
    private Integer pk;      // id объявления
    private Integer price;   // Цена объявления
    private String title; // Заголовок объявления

    public Ad() {
    }

    public Ad(int author, String image, int pk, int price, String title) {
        this.author = author;
        this.image = image;
        this.pk = pk;
        this.price = price;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ad ad = (Ad) o;
        return author == ad.author && pk == ad.pk && price == ad.price && Objects.equals(image, ad.image) && Objects.equals(title, ad.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, image, pk, price, title);
    }

    @Override
    public String toString() {
        return "Ad{" +
                "author=" + author +
                ", image='" + image + '\'' +
                ", pk=" + pk +
                ", price=" + price +
                ", title='" + title + '\'' +
                '}';
    }
}
