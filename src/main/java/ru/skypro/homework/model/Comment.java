package ru.skypro.homework.model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.Objects;
/**
 * Сущность для представления комментария.
 * <p>
 * В классе представлены данные для сущности комментария, а именно: уникальный идентификатор комментария, текстовка,
 * пользователь  и уникальный идентификатор объявления.
 * Также в классе реализованы конструкторы, геттеры, сеттеры, equals, hashCode и toString.
 * </p>
 */
@Entity
@Table(name = "comment")
@Transactional
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Integer id;

    private String text;

    @Column(name = "created_at")
    private long createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private Ad ad;

    @Override
    public String toString() {
        return "Comment{" +
                "pk=" + id +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(text, comment.text) && Objects.equals(createdAt, comment.createdAt) && Objects.equals(user, comment.user) && Objects.equals(ad, comment.ad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, createdAt, user, ad);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer pk) {
        this.id = pk;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public Comment() {
    }
}
