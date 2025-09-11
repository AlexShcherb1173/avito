package ru.skypro.homework.responseDto;

/**
 * определяет, как будет выглядеть ответ сервера, когда фронтенд запрашивает список объявлений
 */

import lombok.Data;

@Data
public class AdDto {

    /**
     * id автора
     */
    private Integer author;

    /**
     * id объявления
     */
    private Integer pk;

    private String image;
    private Integer price;
    private String title;

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}