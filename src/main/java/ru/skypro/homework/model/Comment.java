package ru.skypro.homework.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    private Integer createdAt;
    private String text;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    public Comment() {
    }

    public Comment(User author,
                   String text,
                   Ad ad) {
        this.author = author;
        this.createdAt = (int) (Instant.now().toEpochMilli() / 1000L);
        this.text = text;
        this.ad = ad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(author, comment.author) && Objects.equals(createdAt, comment.createdAt) && Objects.equals(text, comment.text) && Objects.equals(ad, comment.ad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, createdAt, text, ad);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", author=" + author +
                ", createdAt=" + createdAt +
                ", text='" + text + '\'' +
                ", ad=" + ad +
                '}';
    }
}
