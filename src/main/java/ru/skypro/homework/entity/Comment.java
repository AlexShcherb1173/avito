
package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id",nullable = false)
    private Integer id;

    @ManyToOne  (fetch = FetchType.EAGER)
    @JoinColumn (name = "author_id")
    private Users author;

    @ManyToOne  (fetch = FetchType.EAGER)
    @JoinColumn (name = "ad_id")
    private Ad ad;

    @Column(name = "createdAt", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "text", nullable = false)
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(createdAt, comment.createdAt) && Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, text);
    }


    public void setAdId(Integer adId) {
        this.ad = new Ad();
    }

    public void setUpdatedAt(LocalDateTime now) {
        this.createdAt = now;
    }
}

