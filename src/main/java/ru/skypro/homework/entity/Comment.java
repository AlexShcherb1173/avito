package ru.skypro.homework.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.skypro.homework.dto.User.UserDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@Builder
@Schema(description = "Комментарий к объявлению")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private Advertisement ad;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(author, comment.author) && Objects.equals(ad, comment.ad) && Objects.equals(createdAt, comment.createdAt) && Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, ad, createdAt, text);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", author=" + author +
                ", ad=" + ad +
                ", createdAt=" + createdAt +
                ", text='" + text + '\'' +
                '}';
    }

}