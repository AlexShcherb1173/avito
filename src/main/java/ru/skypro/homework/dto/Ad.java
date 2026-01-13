package ru.skypro.homework.dto;

public class Ad {
    private Integer author;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;

    public Ad() {}

    public Ad(Integer author, String image, Integer pk, Integer price, String title) {
        this.author = author;
        this.image = image;
        this.pk = pk;
        this.price = price;
        this.title = title;
    }

    public Integer getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public Integer getPk() {
        return pk;
    }

    public Integer getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}