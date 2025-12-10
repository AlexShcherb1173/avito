package ru.skypro.homework.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;

/**
 * JPA сущность для представления пользователя в базе данных.
 * Соответствует таблице 'users' в базе данных.
 * Содержит информацию о пользователе и его связи с объявлениями и комментариями.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    /**
     * Уникальный идентификатор пользователя.
     * Генерируется автоматически базой данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Email пользователя, используется как логин.
     * Должен быть уникальным и не может быть null.
     */
    @Column(name = "email", nullable = false, unique = true, length = 32)
    private String email;

    /**
     * Имя пользователя.
     * Не может быть null, максимальная длина 16 символов.
     */
    @Column(name = "first_name", nullable = false, length = 16)
    private String firstName;

    /**
     * Фамилия пользователя.
     * Не может быть null, максимальная длина 16 символов.
     */
    @Column(name = "last_name", nullable = false, length = 16)
    private String lastName;

    /**
     * Номер телефона пользователя.
     * Может быть null, максимальная длина 20 символов.
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * Роль пользователя в системе.
     * Определяет уровень доступа пользователя.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * Имя файла аватара пользователя.
     * Хранится в виде строки, может быть null.
     */
    @Column(name = "image")
    private String image;

    /**
     * Хэшированный пароль пользователя.
     * Не может быть null, максимальная длина 255 символов.
     */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * Список объявлений, созданных пользователем.
     * Однонаправленная связь One-to-Many с каскадными операциями.
     */
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AdEntity> ads;

    /**
     * Список комментариев, оставленных пользователем.
     * Однонаправленная связь One-to-Many с каскадными операциями.
     */
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CommentEntity> comments;
}