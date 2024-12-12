package ru.skypro.homework.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer pk;

    private String text;

    @Column(name = "createdat")
    private Integer createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private Ad ad;

    public Comment(Integer pk, String text, Integer createdAt, User user, Ad ad) {
        this.pk = pk;
        this.text = text;
        this.createdAt = createdAt;
        this.user = user;
        this.ad = ad;
    }

    public Comment() {
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(pk, comment.pk) && Objects.equals(text, comment.text) && Objects.equals(createdAt, comment.createdAt) && Objects.equals(user, comment.user) && Objects.equals(ad, comment.ad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk, text, createdAt, user, ad);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "pk=" + pk +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                ", user=" + user +
                ", ad=" + ad +
                '}';
    }
}
