package ru.skypro.homework.dto.user;

import lombok.*;
import ru.skypro.homework.dto.Role;


@Setter
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;

}
