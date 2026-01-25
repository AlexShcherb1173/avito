package ru.skypro.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 64)
    private String email;

    @Column(length = 10)
    private String firstName;

    @Column(length = 10)
    private String lastName;

    @Column(length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 255)
    private String image;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<AdEntity> ads;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<CommentEntity> comments;

    public enum Role {
        USER,
        ADMIN
    }
}
