package ru.skypro.homework.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "ads")
public class AdEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 32)
    private String title;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = 64)
    private String description;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @OneToMany(mappedBy = "ad", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<CommentEntity> comments;
}
