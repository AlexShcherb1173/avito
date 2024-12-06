package ru.skypro.homework.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.skypro.homework.dto.RoleDto;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

//@Table(name = "users")
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Data
//@AllArgsConstructor
//@Entity
//public class UserEntity {
//
//    /*** User Id / Уникальный идентификатор пользователя ***/
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Integer id;
//
//    /*** User login / Логин пользователя ***/
//    @Column(name = "email")
//    private String email;
//
//
//    /*** User name / Имя пользователя ***/
//    @Column(name = "first_name")
//    private String firstName;
//
//    /*** User lastname / Фамилия пользователя ***/
//    @Column(name = "last_name")
//    private String lastName;
//
//    /*** User phone number / Номер телефона пользователя ***/
//    @Column(name = "phone")
//    private String phone;
//
//    /*** User role / Роль пользователя ***/
//    @Enumerated(EnumType.STRING)
//    @Column(name = "role")
//    private RoleDto role;
//
//    /*** User password / Пароль пользователя ***/
//    @Column(name = "password")
//    private String password;
//
//    /*** Image data model / Модель данных изображения ***/
////    @OneToOne
////    @ToString.Exclude
//    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private ImageEntity image;
//
//    /*** User ads set / Набор всех объявлений пользователя ***/
//    @ToString.Exclude
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private Set<AdEntity> ad;
//
//    public UserEntity() {
//
//    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @Column(nullable = false)
//    private String password;
//
//    @Column(nullable = false)
//    private String firstName;
//
//    @Column(nullable = false)
//    private String lastName;
//
//    @Column(nullable = false)
//    private String phone;
//
//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)  //используется для хранения значений типа enum в базе данных в виде строк.
//    private RoleDto role;
//
//    @OneToOne
//    @JoinColumn(name="avatar_id")
//    private AvatarEntity avatar;
//
//
//    public UserEntity() {
//    }
//
//}

@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    @OneToOne
    private AvatarEntity avatar;
    @OneToMany
    private List<CommentEntity> comments;
    @Enumerated(EnumType.STRING)
    private RoleDto role;


    public UserEntity() {

    }
}
