package ru.skypro.homework.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.skypro.homework.dto.Role;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    @Size(max = 50)
    @Pattern(regexp = "^[А-Яа-яA-Za-z\\-\\s]+$", message = "Имя может содержать только буквы, пробелы и дефис")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Size(max = 50)
    @Pattern(regexp = "^[А-Яа-яA-Za-z\\-\\s]+$", message = "Фамилия может содержать только буквы, пробелы и дефис")
    private String lastName;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String image;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ad> Ads;

    @Override
    public boolean equals(Object o){
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(id);
    }

}
