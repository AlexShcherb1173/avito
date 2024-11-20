package ru.skypro.homework.model;

import lombok.Data;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "app_user")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //id пользователя

    @Column(unique = true, nullable = false)
    private String username; // Логин пользователя

    @Column(nullable = false)
    private String password; // Пароль пользователя

    @Column(nullable = false)
    private String firstName; // Имя пользователя

    @Column(nullable = false)
    private String lastName; // Фамилия пользователя

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // Роль пользователя

    private String phone; // Телефон пользователя
    private String image; // Ссылка на аватар

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdModel> adsList; // Список объявлений пользователя

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentModel> commentsList; // Список комментариев пользователя

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_image_id", referencedColumnName = "imageId")
    private ImageModel imageModel; // Связь с моделью изображения
}
