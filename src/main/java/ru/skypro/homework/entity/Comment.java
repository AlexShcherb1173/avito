package ru.skypro.homework.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "advert_id", referencedColumnName = "id")
    private Ad ad;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id && Objects.equals(createdAt, comment.createdAt) && Objects.equals(text, comment.text) && Objects.equals(author, comment.author) && Objects.equals(ad, comment.ad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, text, author, ad);
    }
}
