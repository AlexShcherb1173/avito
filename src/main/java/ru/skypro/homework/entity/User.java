package ru.skypro.homework.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import ru.skypro.homework.dto.Advertisement.AdDTO;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "Пользователи")
@Builder
@Schema(description = "Модель пользователя")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор пользователя")
    private Long id;

    @Schema(description = "Email пользователя")
    private String email;

    @Schema(description = "Имя пользователя")
    private String firstName;

    @Schema(description = "Фамилия пользователя")
    private String lastName;

    @Schema(description = "Пароль пользователя")
    @Column(nullable = false)
    public String password;

    @Schema(description = "Телефон пользователя")
    private String phone;

    @Schema(description = "Роль пользователя", example = "USER")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Advertisement> advertisements;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @Schema(description = "Ссылка на аватар пользователя", example = "/users/image/1")
    private String image;

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", ads=" + advertisements +
                ", comments=" + comments +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User userModel = (User) o;
        return Objects.equals(id, userModel.id) && Objects.equals(email, userModel.email) && Objects.equals(firstName, userModel.firstName) && Objects.equals(lastName, userModel.lastName) && Objects.equals(phone, userModel.phone) && role == userModel.role && Objects.equals(advertisements, userModel.advertisements) && Objects.equals(comments, userModel.comments) && Objects.equals(image, userModel.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, phone, role, advertisements, comments, image);
    }
}