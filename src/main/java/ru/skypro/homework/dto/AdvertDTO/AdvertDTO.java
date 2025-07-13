package ru.skypro.homework.dto.AdvertDTO;

import ru.skypro.homework.dto.CommentDTO.CommentDTO;

import java.time.LocalDateTime;
import java.util.List;
/**
 * DTO для представления объявления
 */
public class AdvertDTO {
    private Long id; // ID объявления
    private String title; // Заголовок объявления
    private String description; // Подробное описание
    private Double price; // Цена товара
    private String[] images; // Массив URL изображений товара
    private UserShortDTO author; // Краткая информация об авторе
    private List<CommentDTO> comments; // Список комментариев
    private LocalDateTime createdAt; // Дата создания объявления

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public UserShortDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserShortDTO author) {
        this.author = author;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
