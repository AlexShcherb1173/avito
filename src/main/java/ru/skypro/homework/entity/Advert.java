package ru.skypro.homework.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.List;
@Entity
@Data
@Table(name = "advert")
public class Advert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = 64)
    private String description;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToOne
    @JoinColumn(name = "image")
    private Image image;
}
