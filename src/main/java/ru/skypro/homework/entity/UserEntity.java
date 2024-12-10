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
