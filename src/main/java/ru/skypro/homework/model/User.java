package ru.skypro.homework.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.skypro.homework.dto.RoleDTO;

import javax.persistence.*;
import java.util.List;

/**
 * Класс, представляющий собой сущность пользователя.
 *
 * Эта сущность содержит информацию о пользователе, включая его уникальный идентификатор,
 * имя пользователя, фамилию, пароль, телефон, роль, аватар, объявления, комментарии и статус.
 *
 * Поле {@code id} содержит уникальный идентификатор пользователя.
 * Поле {@code username} представляет логин пользователя, который должен быть уникальным.
 * Поля {@code firstName} и {@code lastName} содержат имя и фамилию пользователя.
 * Поле {@code password} содержит пароль пользователя.
 * Поле {@code phone} представляет номер телефона пользователя, который должен быть уникальным.
 * Поле {@code role} указывает роль пользователя, которая определяется с помощью перечисления {@link RoleDTO}.
 * Поле {@code image} хранит ссылку на аватар пользователя.
 * Поле {@code ads} содержит список объявлений, принадлежащих пользователю (связь один ко многим).
 * Поле {@code comments} содержит список комментариев, оставленных пользователем (связь один ко многим).
 * Поле {@code enabled} указывает, активен ли пользователь.
 *
 * Аннотация {@link OneToMany} устанавливает связи с сущностями {@link Ad} (объявления) и {@link Comment} (комментарии).
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", unique = true)
    private String username;

    private String firstName;

    private String lastName;

    private String password;

    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    private RoleDTO role;

    private String image;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ad> ads;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    private Boolean enabled;
}

