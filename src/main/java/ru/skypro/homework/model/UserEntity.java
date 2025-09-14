package ru.skypro.homework.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "users")
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    @Column(length = 32)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    private String image;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<AdEntity> ads;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<CommentEntity> comments;
}
