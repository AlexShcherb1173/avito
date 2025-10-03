package ru.skypro.homework.model;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String image;

    public enum Role {
        USER, ADMIN
    }
}
