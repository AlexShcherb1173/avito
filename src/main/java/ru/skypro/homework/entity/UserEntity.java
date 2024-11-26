package ru.skypro.homework.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "f_name")
    private String firstname;

    @Column(name = "s_name")
    private String surname;

    @Column(name = "l_name")
    private String lastname;

    @Column(name = "login")
    private String login;

    @Column(name = "phone")
    private String phone;

    @Column(name = "reg_date")
    private LocalDateTime registrationDate;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "password")
    private String password;
}
