package ru.skypro.homework.dto.rq.user;

import lombok.Data;
import ru.skypro.homework.dto.Role;

@Data
public class Register {
    /**
     *  Поля относящиеся к сущности UserEntity
     */
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    /**
     *  Поля относящиеся к ENUM RoleEntity
     */
    private Role role;


}
