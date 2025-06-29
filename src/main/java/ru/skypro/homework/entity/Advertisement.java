package ru.skypro.homework.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.skypro.homework.dto.User.UserDTO;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Builder
@Schema(description = "Модель объявления")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer price;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @Column(nullable = false)
    private String imagePath;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advertisement that = (Advertisement) o;
        return Objects.equals(id, that.id) && Objects.equals(author, that.author) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(comments, that.comments) && Objects.equals(imagePath, that.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, title, description, price, comments, imagePath);
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "id=" + id +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", comments=" + comments +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

}