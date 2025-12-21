package ru.skypro.homework.dto;

import lombok.*;

import static ru.skypro.homework.dto.Role.USER;
@Data
@Getter
@Setter
@AllArgsConstructor // создает конструктор
@NoArgsConstructor  // создает пустой конструктор
public class User {
    private Integer id;         // ID пользователя
    private String email;        // Email (логин)
    private String firstName;    // Имя
    private String lastName;     // Фамилия
    private String phone;        // Телефон
    private Role role;         // Роль
    private String image;// Ссылка на аватар

}
