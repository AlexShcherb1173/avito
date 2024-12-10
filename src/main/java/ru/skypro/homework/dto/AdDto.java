package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

public class AdDto {
    @Schema(type = "integer",
            format = "int32",
            description = "id автора объявления")
    Integer id;

    @Schema(type = "string",
            description = "ссылка на картинку объявления")
    Integer image;
    @Schema(type = "integer",
            format = "int32",
            description = "id объявления")
    Integer pk;

    @Schema(type = "integer",
            format = "int32",
            description = "id объявления")
    Integer price;

@Schema(type = "string",
            description = "заголовок объявления")
    Integer title;

    public AdDto(Integer id, Integer image, Integer pk, Integer price, Integer title) {
        this.id = id;
        this.image = image;
        this.pk = pk;
        this.price = price;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTitle() {
        return title;
    }

    public void setTitle(Integer title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdDto)) return false;
        AdDto adDto = (AdDto) o;
        return Objects.equals(id, adDto.id) && Objects.equals(image, adDto.image) && Objects.equals(pk, adDto.pk) && Objects.equals(price, adDto.price) && Objects.equals(title, adDto.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image, pk, price, title);
    }

    @Override
    public String toString() {
        return "Ad{" +
                "id=" + id +
                ", image=" + image +
                ", pk=" + pk +
                ", price=" + price +
                ", title=" + title +
                '}';
    }
}
