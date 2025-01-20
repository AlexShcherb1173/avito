package ru.skypro.homework.entity;

import lombok.Data;
import lombok.ToString;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;

@Data
@ToString
@Entity
@Table(name = "user_ads")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userName;

    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    @OneToOne
    @JoinColumn(name = "image")
    private Image image;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author")
    private List<Ads> adsList;

    @OneToMany(mappedBy = "author")
    private List<Comment> commentsList;
}