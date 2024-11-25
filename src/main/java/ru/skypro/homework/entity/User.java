package ru.skypro.homework.entity;

import lombok.*;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;

/**
 * <b>Представляет сущность пользователя в приложении. </b> <p>
 * Пользователь имеет уникальный идентификатор, контактные данные,
 * пароль и роль в системе. Также пользователь может иметь аватар.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * <b>Уникальный идентификатор пользователя. </b> <p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * <b>Электронная почта пользователя. </b> <p>
     */
    private String email;

    /**
     * <b>Имя пользователя. </b> <p>
     */
    private String firstName;

    /**
     * <b>Фамилия пользователя. </b> <p>
     */
    private String lastName;

    /**
     * <b>Номер телефона пользователя. </b> <p>
     */
    private String phone;

    /**
     * <b>Пароль пользователя. </b> <p>
     */
    private String password;

    /**
     * <b>Роль пользователя в системе. </b> <p>
     * Использует перечисление {@link Role} для определения уровня доступа.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * <b>Аватар пользователя. </b> <p>
     * Это связь один-к-одному, где каждый пользователь может иметь один аватар.
     */
    @OneToOne
    private Avatar avatar;

}
