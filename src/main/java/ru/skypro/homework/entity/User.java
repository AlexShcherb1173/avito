package ru.skypro.homework.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.relation.Role;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "role")
    private Role role;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Ad> ads;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(role, user.role) && Objects.equals(image, user.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, firstName, lastName, phoneNumber, role, image);
    }
}
