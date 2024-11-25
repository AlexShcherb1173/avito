package ru.skypro.homework.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import javax.persistence.Entity;

@Data
@Entity
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //id объявления
    private Integer author; //id автора
    private String image; //ссылка на картинку объявления
    private Integer price; //цена объявления
    private String title; //заголовок объявления
    private String description; //описание

    //2
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user; //связь

    //3
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> commentsList;

    @OneToOne
    @JoinColumn(name = "ad_image_id", referencedColumnName = "imageId")
    private ImageEntity imageEntity;
}
