package ru.skypro.homework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "graduate", name = "ads")
public class AdEntity {
    /**
     * Id объявления
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Медиа файл объявления
     */
    @Column(name = "image", nullable = false)
    private String image;

    /**
     * Цена объявления
     */
    @Column(name = "price", nullable = false)
    private Integer price;

    /**
     * Название объявления
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Описание объявления
     */
    @Column(name = "description", nullable = false)
    private String description;


    /**
     * Список комментариев объявления
     */
    @JsonIgnore
    @OneToMany(mappedBy = "ad", fetch = FetchType.LAZY)
    private List<CommentEntity> comments;


    /**
     * Автор объявления
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity author;
}
