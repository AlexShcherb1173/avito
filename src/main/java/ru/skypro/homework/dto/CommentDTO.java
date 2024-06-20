package ru.skypro.homework.dto;

import java.time.LocalDateTime;

public class CommentDTO {
    private Integer id;
    private Integer authorId;
    private LocalDateTime time;
    private String authorName;
    private String authorAvatar;
    private String text;

    public CommentDTO(Integer authorId, LocalDateTime time, String authorName, String authorAvatar, String text) {
        this.authorId = authorId;
        this.time = time;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
