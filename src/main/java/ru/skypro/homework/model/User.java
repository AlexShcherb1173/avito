package ru.skypro.homework.model;

import jakarta.persistence.*;
import lombok.*;
import ru.skypro.homework.dto.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_username", columnList = "username")
})
public class User extends ParentIDEntity {
    /**
     * логин пользователя
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    /**
     * пароль пользователя
     */
    @Column(name = "password", nullable = false)
    private String password;
    /**
     * имя пользователя
     */
    @Column(name = "firstName")
    private String firstName;
    /**
     * фамилия пользователя
     */
    @Column(name = "lastName")
    private String lastName;
    /**
     * телефон пользователя
     */
    @Column(name = "phone")
    private String phone;
    /**
     * роль пользователя
     */
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
    /**
     * аватар пользователя
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private Image avatar;

    @Builder
    public User(Long id, String username, String password, String firstName, String lastName, String phone, Role role, Image avatar) {
        super(id);
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.avatar = avatar;
    }
}
