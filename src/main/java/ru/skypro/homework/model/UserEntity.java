package ru.skypro.homework.model;

import lombok.Data;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "app_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //id пользователя

    @Column(unique = true, nullable = false)
    private String username; //логин пользователя

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Role role;

    private String phone;
    private String image; //ссылка на аватар

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdEntity> adsList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> commentsList;

    @OneToOne(fetch = FetchType.LAZY) //Spring Security не очень хочет, чтобы загрузка связных объектов была сразу
    @JoinColumn(name = "user_image_id", referencedColumnName = "imageId")
    private ImageEntity imageEntity;
}
