package ru.skypro.homework.model;


import lombok.*;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.skypro.homework.dto.RoleDto;

import javax.management.relation.Role;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Уникальный идентификатор пользователя (первичный ключ).
     * Генерируется автоматически при добавлении записи в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /**
     * Электронная почта пользователя (логин).
     * Поле обязательно для заполнения и должно быть уникальным.
     */
    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Некорректный формат электронной почты")
    @NotBlank(message = "Электронная почта обязательна")
    private String email;

    /**
     * Имя пользователя.
     * Поле обязательно для заполнения.
     */
    @Column(name = "first_name", length = 255, nullable = false)
    @NotBlank(message = "Имя обязательно")
    private String firstName;

    /**
     * Фамилия пользователя.
     * Поле обязательно для заполнения.
     */
    @Column(name = "last_name", length = 255, nullable = false)
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    /**
     * Номер телефона пользователя.
     * Поле обязательно для заполнения.
     */
    @Column(name = "phone", length = 20, nullable = false)
    @NotBlank(message = "Телефон обязателен")
    private String phone;

    /**
     * Роль пользователя (например, USER, ADMIN).
     * Поле обязательно для заполнения.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @NotNull(message = "Роль обязательна")
    private RoleDto roleDto; // Используем enum вместо DTO

    /**
     * Ссылка на аватар пользователя.
     * Поле обязательно для заполнения.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id", nullable = false)
    @NotNull(message = "Изображение обязательно")
    private Image image;

    /**
     * Пароль пользователя.
     * Поле обязательно для заполнения.
     * Должен храниться в зашифрованном виде.
     */
    @Column(name = "password", nullable = false)
    @NotNull(message = "Пароль обязателен")
    private String password;
}