package ru.skypro.homework.entity;

import lombok.*;
//import com.example.exampleforgraduatework.dto.Role;
import ru.skypro.homework.dto.Role;

import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
//этот класс станет entity
public class Users {
    private Integer id;
    private String email;
    private String image;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}