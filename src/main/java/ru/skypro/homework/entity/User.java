package ru.skypro.homework.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.skypro.homework.config.WebSecurityConfig;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Objects;

@Slf4j
@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString(includeFieldNames=true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonProperty("email")
    @Email
    private String email;
    @JsonProperty("firstName")
    @Size(min = 2, max = 16)
    private String firstName;
    @Size(min = 2, max = 16)
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("phone")
    private String phone;
//    @ManyToOne
    @Enumerated(EnumType.STRING)
    @JsonProperty("role")
    private Role role ; // множество пользователей - одна роль
    @JsonProperty("username")
    @Size(min = 4, max = 32)
    private String username;
    @JsonProperty("password")
    @Size(min = 8, max = 16)
    private String password;
    private boolean enabled;
    @OneToOne
    private Image image; // один пользователь - одно изображение

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(phone, user.phone) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, username);
    }
}
