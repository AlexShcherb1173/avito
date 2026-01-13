package ru.skypro.homework.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ads")
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer price;

    @Column
    private String description;

    @Column
    private String image;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public AdEntity() {}

    public AdEntity(Integer pk, UserEntity author, String title, Integer price,
                    String description, String image, LocalDateTime createdAt) {
        this.pk = pk;
        this.author = author;
        this.title = title;
        this.price = price;
        this.description = description;
        this.image = image;
        this.createdAt = createdAt;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        private UserEntity author;
        private String title;
        private Integer price;
        private String description;
        private String image;
        private LocalDateTime createdAt;

        public Builder pk(Integer pk) {
            this.pk = pk;
            return this;
        }

        public Builder author(UserEntity author) {
            this.author = author;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder price(Integer price) {
            this.price = price;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AdEntity build() {
            return new AdEntity(pk, author, title, price, description, image, createdAt);
        }
    }
}