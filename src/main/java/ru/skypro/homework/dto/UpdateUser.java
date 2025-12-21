package ru.skypro.homework.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor // создает конструткор
@NoArgsConstructor // пустой конструктор
public class UpdateUser {
    private String firstName; // Новое имя
    private String lastName;  // Новая фамилия
    private String phone ;// Новый телефон

}
