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
public class AdModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //id объявления
    private Integer author; //id автора
    private String image; //ссылка на картинку объявления
    private MultipartFile imageUpdate; //поле для обновления картинки
    private Integer price; //цена объявления
    private String title; //заголовок объявления
    private String description; //описание объявления

    //2
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user; //связь

    //3
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentModel> commentsList;

    @OneToOne
    @JoinColumn(name = "ad_image_id", referencedColumnName = "imageId")
    private ImageModel imageModel;

    //Метод для получения владельца объявления
    public UserModel getOwner() {
        return this.user; //Возвращаем объект пользователя, который является владельцем объявления
    }
}
