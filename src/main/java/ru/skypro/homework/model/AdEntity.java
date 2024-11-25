package ru.skypro.homework.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import javax.persistence.Entity;

@Data
@NoArgsConstructor
@Entity
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // id объявления

    private Integer author; // id автора
    private String image; // Ссылка на картинку объявления
    private Integer price; // Цена объявления
    private String title; // Заголовок объявления
    private String description; // Описание объявления

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // Связь

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> commentsList; // Список комментариев

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_image_id", referencedColumnName = "imageId")
    private ImageEntity imageEntity; // Связь с моделью изображения
}
