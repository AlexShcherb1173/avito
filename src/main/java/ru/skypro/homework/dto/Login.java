package ru.skypro.homework.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor // создает конструктор
@NoArgsConstructor  // создает пустой конструктор
public class Login {
    private String username;
    private String password;
}
