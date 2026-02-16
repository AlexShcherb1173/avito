package ru.skypro.homework.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    @NotNull
    private int author;
    @NotNull
    @Lob
    @Column(name = "image", columnDefinition = "bytea")
    private byte[] image;
    @NotNull
    private int price;
    @Column(nullable = false)
    private String title;

    public Ad(int author, byte[] image, int price, String title) {
        this.author = author;
        this.image = image;
        this.price = price;
        this.title = title;
    }

    public Ad() {}

    @NotNull
    public int getAuthor() {
        return author;
    }

    public void setAuthor(@NotNull int author) {
        this.author = author;
    }

    public @NotNull byte[] getImage() {
        return image;
    }

    public void setImage(@NotNull byte[] image) {
        this.image = image;
    }

    @NotNull
    public int getPk() {
        return pk;
    }

    @NotNull
    public int getPrice() {
        return price;
    }

    public void setPrice(@NotNull int price) {
        this.price = price;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }
}
