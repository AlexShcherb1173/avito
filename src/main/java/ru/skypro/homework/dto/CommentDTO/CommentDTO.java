package ru.skypro.homework.dto.CommentDTO;

import ru.skypro.homework.dto.AdvertDTO.UserShortDTO;

import java.time.LocalDateTime;
/**
 * DTO для представления комментария
 */
public class CommentDTO {
    private Long id; // ID комментария
    private String text; // Текст комментария
    private UserShortDTO author; // Автор комментария
    private LocalDateTime createdAt; // Дата создания

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserShortDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserShortDTO author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
