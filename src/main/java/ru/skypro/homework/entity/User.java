package ru.skypro.homework.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import ru.skypro.homework.dto.Role;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    //@NotBlank
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @Column(nullable = false)
    private String phone;

    @ColumnDefault(value = "0")
    @Column(name = "role", nullable = false, columnDefinition = "int")
    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @Column(name = "image_path")
    private String image;
    // Путь к файлу на диске, состоящий только из "/" и имени файла


}
