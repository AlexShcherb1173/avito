package ru.skypro.homework.model;

import lombok.*;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;

/**
 * {@link Класс} User автора объявления <br>
 * {@link Integer} id - идентификатор автора (not null) <br>
 * {@link String} username - логин автора (not null) <br>
 * {@link String} password - пароль автора (not null) <br>
 * {@link String} firstname - имя автора (not null) <br>
 * {@link String} lastname - фамилия автора (not null) <br>
 * {@link String} phone  - телефон (not null) <br>
 * {@link Role} role - роль (not null) <br>
 * {@link Byte} image - аватарка автора (not null) <br>
 * {@link List<Ad>} ads - список объявлений автора <br>
 * {@link List<Comment>} commentsList - список комментариев автора <br>
 *
 * @author Archinski
 */
@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ad> ads;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> commentsList;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_image_id", referencedColumnName = "id")
    private Image image;
}
