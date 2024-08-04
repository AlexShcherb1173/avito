package ru.skypro.homework.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "adds")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private int price;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.REMOVE)
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ad ad = (Ad) o;
        return id == ad.id && price == ad.price && Objects.equals(title, ad.title) && Objects.equals(description, ad.description) && Objects.equals(author, ad.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, price, author);
    }
}
