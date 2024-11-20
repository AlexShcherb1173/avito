package ru.skypro.homework.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;
import javax.persistence.Entity;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ad_model")
public class AdModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adId; // id объявления

    private Integer author; // id автора
    private String image; // Ссылка на картинку объявления
    private Integer price; // Цена объявления
    private String title; // Заголовок объявления
    private String description; // Описание объявления

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user; // Связь

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentModel> commentsList; // Список комментариев

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_image_id", referencedColumnName = "imageId")
    private ImageModel imageModel; // Связь с моделью изображения

    @Transient // Указывает, что это поле не должно сохраняться в базе данных
    private MultipartFile imageUpdate; // Поле для обновления картинки

    // Метод для получения владельца объявления
    public UserModel getOwner() {
        return this.user; // Возвращаем объект пользователя, который является владельцем объявления
    }

    // Дополнительное поле для хранения пути обновленного изображения
    private String imageUpdatePath;
}
