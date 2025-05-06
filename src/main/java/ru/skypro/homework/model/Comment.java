package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * Сущность комментария (Comment).
 */
@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "text")
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id)
                && Objects.equals(ad, comment.ad)
                && Objects.equals(author, comment.author)
                && Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ad, author, text);
    }

    @Override
    public String toString() {
        return "Comment{id=" + id +
                ", text='" + text + "'}";
    }
}
