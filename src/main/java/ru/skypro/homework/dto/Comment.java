package ru.skypro.homework.dto;

public class Comment {
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Long createdAt;
    private Integer pk;
    private String text;

    public Comment() {}

    public Comment(Integer author, String authorImage, String authorFirstName,
                   Long createdAt, Integer pk, String text) {
        this.author = author;
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
        this.createdAt = createdAt;
        this.pk = pk;
        this.text = text;
    }

    public Integer getAuthor() {
        return author;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Integer getPk() {
        return pk;
    }

    public String getText() {
        return text;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public void setText(String text) {
        this.text = text;
    }
}