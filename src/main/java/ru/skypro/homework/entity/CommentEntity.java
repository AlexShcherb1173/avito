package ru.skypro.homework.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    @ManyToOne
    @JoinColumn(name = "ad_id", nullable = false)
    private AdEntity ad;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public CommentEntity() {}

    public CommentEntity(Integer pk, AdEntity ad, UserEntity author, String text, LocalDateTime createdAt) {
        this.pk = pk;
        this.ad = ad;
        this.author = author;
        this.text = text;
        this.createdAt = createdAt;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public AdEntity getAd() {
        return ad;
    }

    public void setAd(AdEntity ad) {
        this.ad = ad;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer pk;
        private AdEntity ad;
        private UserEntity author;
        private String text;
        private LocalDateTime createdAt;

        public Builder pk(Integer pk) {
            this.pk = pk;
            return this;
        }

        public Builder ad(AdEntity ad) {
            this.ad = ad;
            return this;
        }

        public Builder author(UserEntity author) {
            this.author = author;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CommentEntity build() {
            return new CommentEntity(pk, ad, author, text, createdAt);
        }
    }
}