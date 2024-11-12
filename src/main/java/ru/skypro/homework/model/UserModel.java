package ru.skypro.homework.model;

import lombok.Data;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "app_user")
public class UserModel {

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
    @Enumerated(EnumType.STRING)
    private Role role;

    private String phone;
    private String image; //ссылка на аватар

    //1
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdModel> adsList;

    //2
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentModel> commentsList;

    @OneToOne
    @JoinColumn(name = "user_image_id", referencedColumnName = "imageId")
    private ImageModel imageModel;
}
