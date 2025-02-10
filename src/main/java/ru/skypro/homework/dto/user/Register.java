package ru.skypro.homework.dto.user;


import lombok.Data;
import ru.skypro.homework.dto.Role;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Принимаемые данные для регистрации нового пользователя
 */
@Data
public class Register {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    private Role role;
}
