package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.skypro.homework.dto.Role;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 32)
    private String email;

    @Column(nullable = false, length = 100)  // Увеличено для хэша пароля
    private String password;

    @Column(name = "first_name", nullable = false, length = 32)  // Увеличено с 16 до 32
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 32)  // Увеличено с 16 до 32
    private String lastName;

    @Column(nullable = false, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)  // Увеличено с 5 до 10
    private Role role;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Ad> ads;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Comment> comments;
}